package com.android.dev.sineth.ytsmoviefactory.View;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.dev.sineth.ytsmoviefactory.Database.DBHelper;
import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.Models.Torrent;
import com.android.dev.sineth.ytsmoviefactory.Network.InputStreamVolleyRequest;
import com.android.dev.sineth.ytsmoviefactory.Network.VolleySingleton;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.Resources.Keys;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

public class ViewMovie extends AppCompatActivity implements View.OnClickListener {
    private final String MOVIE_DETAILS_URL = "https://yts.ag/api/v2/movie_details.json?movie_id=";
    private final String CAST_URL = "&with_images=true&with_cast=true";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView main_textView;
    private Movie movie;
    private SQLiteDatabase sqLiteDatabase;
    private RequestQueue requestQueue;
    private volatile List<Torrent> torrentList = new Vector<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);
/**
 * layout items initialization by  binding with the variables
 */
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView coverImage = (ImageView) findViewById(R.id.backdrop);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        main_textView = (TextView) findViewById(R.id.movieDescriptionTextView);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        movie = getIntent().getExtras().getParcelable("Movie");

        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        setSupportActionBar(toolbar);
        if (movie != null) {
            collapsingToolbarLayout.setTitle(movie.getMovie_title());
            main_textView.setText(movie.getSummary());

        } else {
            showErrorDialog(getString(R.string.errorTitle),getString(R.string.errorContent));
        }

        getMovieCoverLargePoster(coverImage, imageLoader);

//        for (Torrent t:movie.getTorrentList()){
//            switch (t.getQuality()){
//                case "720p":
//                    String size_720 ="Size "+t.getSize();
//                    ((TextView) findViewById(R.id.size_720p)).setText(size_720);
//                    String peers_720 ="Peers "+t.getPeers();
//                    ((TextView)findViewById(R.id.peers_720p)).setText(peers_720);
//                    String seeds_720="seeds "+t.getSeeds();
//                    ((TextView)findViewById(R.id.seeds_720p)).setText(seeds_720);
//                    break;
//                case "1080p":
//                    String size_1080 ="Size "+t.getSize();
//                    ((TextView) findViewById(R.id.size_1080p)).setText(size_1080);
//                    String peers_1080="Peers "+t.getPeers();
//                    ((TextView)findViewById(R.id.peers_1080p)).setText(peers_1080);
//                    String seeds_1080 ="seeds "+t.getSeeds();
//                    ((TextView)findViewById(R.id.seeds_1080p)).setText(seeds_1080);
//                    break;
//                case "3D":
//                    String size_3D ="Size "+t.getSize();
//                    ((TextView) findViewById(R.id.size_3d)).setText(size_3D);
//                    String peers_3D ="Peers "+t.getPeers();
//                    ((TextView)findViewById(R.id.peers_3d)).setText(peers_3D);
//                    String seeds_3D ="seeds "+t.getSeeds();
//                    Log.i("1080p_peers",peers_3D);
//                    ((TextView)findViewById(R.id.seeds_3d)).setText(seeds_3D);
//                    break;
//                default:
//                    Snackbar.make(null,"Some Torrent Details are not available ",Snackbar.LENGTH_LONG).show();
//            }
//
//        }
        ((TextView) findViewById(R.id.movie_genre_view_movie)).setText(getString(R.string.Genres) + movie.getMovie_genre());
        ((TextView) findViewById(R.id.downloads)).setText("Downloaded " + movie.getDownloads() + " times");
        ((TextView) findViewById(R.id.yt_url)).setText("https://www.youtube.com/watch?v=" + movie.getYouTubeurl());


        Log.d("fav", String.valueOf(movie.getFavourite()));
        if (movie.getFavourite()) {
            fab.setImageResource(R.mipmap.ic_fav_added);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBHelper dbHelper = new DBHelper(ViewMovie.this);
                sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Keys.ID, movie.getId());
                values.put(Keys.TITLE, movie.getMovie_title());
                values.put(Keys.YEAR, movie.getMovie_released_year());
                values.put(Keys.RATING, movie.getMovie_rating());
                values.put(Keys.GENRES, movie.getMovie_genre());
                values.put(Keys.SUMMARY, movie.getSummary());
                values.put(Keys.LARGE_COVER_IMAGE, movie.getMovie_cover_url_large());
                values.put(Keys.MEDIUM_COVER_IMAGE, movie.getMovie_cover_url_medium());
                values.put(Keys.SMALL_COVER_IMAGE, movie.getMovie_cover_url_small());
                values.put(Keys.YOUTUBE_TRAILER, movie.getYouTubeurl());
                long rowCount = sqLiteDatabase.insert(DBHelper.MOVIE, null, values);
                if (rowCount > 0) {
                    Snackbar.make(view, "Successfully Added to Favourites", Snackbar.LENGTH_LONG).setAction("Undo", this).show();
                    fab.setImageResource(R.mipmap.ic_fav_added);
                    movie.setFavourite(true);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        getAllDetails();
      //  getTorrent();
        Toast.makeText(ViewMovie.this,torrentList.size()+"",Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog(String title,String content) {
        new MaterialDialog.Builder(ViewMovie.this)
                .title(title)
                .content(content)
                .positiveText(R.string.back)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                })
                .negativeText(R.string.dismiss)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Downloads movie cover poster asynchronously if an error occurs user is
     * notified.
     *
     * @param coverImage
     * @param imageLoader
     */
    private void getMovieCoverLargePoster(final ImageView coverImage, ImageLoader imageLoader) {
        imageLoader.get(movie.getMovie_cover_url_large(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bm = response.getBitmap();
                coverImage.setImageBitmap(bm);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                //        Snackbar.make(view,"We are having issues downloading the movie poster",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllDetails() {
        String URL = MOVIE_DETAILS_URL + movie.getId() + CAST_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dataObject = response.getJSONObject(Keys.DATA);
                    JSONObject movieObject = dataObject.getJSONObject(Keys.MOVIE);
                    JSONArray torrentsArray = movieObject.getJSONArray(Keys.TORRENTS);
                    for (int i = 0; i < torrentsArray.length(); i++) {
                        JSONObject torrentObject = torrentsArray.getJSONObject(i);
                        Torrent torrent = new Torrent();
                        torrent.setUrl(torrentObject.getString(Keys.URL));
                        torrent.setSize(torrentObject.getString(Keys.SIZE));
                        torrent.setQuality(torrentObject.getString(Keys.QUALITY));
                        torrent.setSeeds(Integer.parseInt(torrentObject.getString(Keys.SEEDS)));
                        torrent.setPeers(Integer.parseInt(torrentObject.getString(Keys.PEERS)));
                        addToList(torrent);
                    }
//                    getTorrent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private synchronized void addToList(Torrent torrent) {
        this.torrentList.add(torrent);
    }

    @Override
    public void onClick(View v) {
        sqLiteDatabase.delete(DBHelper.MOVIE, Keys.ID, new String[movie.getId()]);

    }

    private void getTorrent() {
        String mUrl = torrentList.get(0).getUrl();
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response != null) {
                                String name = movie.getMovie_title() + ".torrent";
                                File torrentDirectory = createFolder();
                                File file = new File(torrentDirectory, name);
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(response);
                                Toast.makeText(ViewMovie.this,"Successfully Downloaded",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Torrents", movie.getMovie_title() + ".torrent")), "application/x-bittorrent");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                boolean available = isAvailable(ViewMovie.this, intent);
                                Log.e("App Available", String.valueOf(available));
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
            }
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }

    public File createFolder() {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Torrents");
        if (!dir.exists()) {
            Toast.makeText(ViewMovie.this, " ! Exists", Toast.LENGTH_SHORT).show();
            try {
                if (dir.mkdir()) {
                    Toast.makeText(ViewMovie.this, "Success", Toast.LENGTH_SHORT).show();
                    return dir;
                } else {
                    Toast.makeText(ViewMovie.this, "Failed" + Environment.getExternalStorageDirectory().getPath(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return dir;
        }
        return null;
    }

    public boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
