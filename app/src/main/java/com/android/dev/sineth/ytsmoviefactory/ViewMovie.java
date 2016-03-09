package com.android.dev.sineth.ytsmoviefactory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.dev.sineth.ytsmoviefactory.Database.DBHelper;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class ViewMovie extends AppCompatActivity implements View.OnClickListener{
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView main_textView;
    private MovieBriefDetails movie;
    private SQLiteDatabase sqLiteDatabase;
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
        main_textView= (TextView) findViewById(R.id.movieDescriptionTextView);
        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        movie = (MovieBriefDetails) getIntent().getExtras().getSerializable("Movie");
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        setSupportActionBar(toolbar);


        if (movie != null) {
            collapsingToolbarLayout.setTitle(movie.getMovie_title());
            main_textView.setText(movie.getSummary());

        }else{
            new MaterialDialog.Builder(ViewMovie.this)
                    .title("Error while loading content")
                    .content("We are having trouble loading movie details.")
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

        getMovieCoverLargePoster(coverImage, imageLoader,this.findViewById(android.R.id.content));

        for (Torrent t:movie.getTorrentList()){
            switch (t.getQuality()){
                case "720p":
                    String size_720 ="Size "+t.getSize();
                    ((TextView) findViewById(R.id.size_720p)).setText(size_720);
                    String peers_720 ="Peers "+t.getPeers();
                    ((TextView)findViewById(R.id.peers_720p)).setText(peers_720);
                    String seeds_720="seeds "+t.getSeeds();
                    ((TextView)findViewById(R.id.seeds_720p)).setText(seeds_720);
                    break;
                case "1080p":
                    String size_1080 ="Size "+t.getSize();
                    ((TextView) findViewById(R.id.size_1080p)).setText(size_1080);
                    String peers_1080="Peers "+t.getPeers();
                    ((TextView)findViewById(R.id.peers_1080p)).setText(peers_1080);
                    String seeds_1080 ="seeds "+t.getSeeds();
                    ((TextView)findViewById(R.id.seeds_1080p)).setText(seeds_1080);
                    break;
                case "3D":
                    String size_3D ="Size "+t.getSize();
                    ((TextView) findViewById(R.id.size_3d)).setText(size_3D);
                    String peers_3D ="Peers "+t.getPeers();
                    ((TextView)findViewById(R.id.peers_3d)).setText(peers_3D);
                    String seeds_3D ="seeds "+t.getSeeds();
                    Log.i("1080p_peers",peers_3D);
                    ((TextView)findViewById(R.id.seeds_3d)).setText(seeds_3D);
                    break;
                default:
                    Snackbar.make(null,"Some Torrent Details are not available ",Snackbar.LENGTH_LONG).show();
            }
            ((TextView)findViewById(R.id.movie_genre_view_movie)).setText("Genres: "+movie.getMovie_genre());
            ((TextView)findViewById(R.id.rt_rating)).setText("Rotten Tomatoes rating: "+movie.getMovie_rating());
            ((TextView)findViewById(R.id.downloads)).setText("Downloaded "+movie.getDownloads()+" times");
            ((TextView)findViewById(R.id.yt_url)).setText("https://www.youtube.com/watch?v="+movie.getYouTubeurl());
        }



        Log.d("fav", String.valueOf(movie.getFavourite()));
        if (movie.getFavourite()){
            fab.setImageResource(R.mipmap.ic_fav_added);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBHelper dbHelper=new DBHelper(ViewMovie.this);
                sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(Keys.ID,movie.getId());
                values.put(Keys.TITLE,movie.getMovie_title());
                values.put(Keys.YEAR,movie.getMovie_released_year());
                values.put(Keys.RATING,movie.getMovie_rating());
                values.put(Keys.GENRES,movie.getMovie_genre());
                values.put(Keys.SUMMARY,movie.getSummary());
                values.put(Keys.LARGE_COVER_IMAGE,movie.getMovie_cover_url_large());
                values.put(Keys.MEDIUM_COVER_IMAGE,movie.getMovie_cover_url_medium());
                values.put(Keys.SMALL_COVER_IMAGE,movie.getMovie_cover_url_small());
                values.put(Keys.YOUTUBE_TRAILER,movie.getYouTubeurl());
                long rowCount = sqLiteDatabase.insert(DBHelper.MOVIE, null, values);
                if (rowCount>0){
                    Snackbar.make(view, "Successfully Added to Favourites", Snackbar.LENGTH_LONG).setAction("Undo",this).
                            setActionTextColor(getColor(android.R.color.holo_red_dark)).show();
                    fab.setImageResource(R.mipmap.ic_fav_added);
                    movie.setFavourite(true);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Downloads movie cover poster asynchronously if an error occurs user is
     * notified.
     * @param coverImage
     * @param imageLoader
     */
    private void getMovieCoverLargePoster(final ImageView coverImage, ImageLoader imageLoader,final View view) {
        imageLoader.get(movie.getMovie_cover_url_large(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bm = response.getBitmap();
                coverImage.setImageBitmap(bm);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view,"We are having issues downloading the movie poster",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        sqLiteDatabase.delete(DBHelper.MOVIE,Keys.ID,new String[movie.getId()]);

    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
