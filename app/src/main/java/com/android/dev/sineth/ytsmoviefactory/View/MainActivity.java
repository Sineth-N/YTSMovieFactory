package com.android.dev.sineth.ytsmoviefactory.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.dev.sineth.ytsmoviefactory.Kernel.Core;
import com.android.dev.sineth.ytsmoviefactory.Kernel.JSONParser;
import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.Network.VolleySingleton;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.RecycleViewAdapter.RecycleViewAdapterMovie;
import com.android.dev.sineth.ytsmoviefactory.Resources.Keys;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String URL = "https://yts.ag/api/v2/list_movies.json?";
    private static int page = 1;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Movie> list_movies = new ArrayList<>();
    private ArrayList<Movie> test = new ArrayList<>();
    private RecycleViewAdapterMovie recycleViewAdapterMovie;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HashSet<Integer> set = new HashSet<>();
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting up main toolbars and the actionbars

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //network things
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        recyclerView = (RecyclerView) findViewById(R.id.movieList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
//        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recycleViewAdapterMovie = new RecycleViewAdapterMovie(MainActivity.this);
        recyclerView.setAdapter(recycleViewAdapterMovie);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Fetching More Movies, please Wait.", Snackbar.LENGTH_SHORT)
                        .setAction("Dismiss", null).show();
                loadMore(5);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light
        );

        if (savedInstanceState != null) {
            this.list_movies = (ArrayList<Movie>) savedInstanceState.getParcelable("list");
        } else {
            loadMore(5);

        }

    }

    private void sendJSONRequest() {
        final MaterialDialog progressBar = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_fetch)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONParser.parseJSONResponse(response,MainActivity.this);
                list_movies= Core.getMovies();
                Collections.sort(list_movies, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie lhs, Movie rhs) {
                        return lhs.compare(lhs, rhs);
                    }
                });
                recycleViewAdapterMovie.setMovieList(list_movies);
                recycleViewAdapterMovie.notifyDataSetChanged();
                progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NetworkError) {
                    MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                            .title("Timeout Error")
                            .content("Please check your network connectivity")
                            .positiveText(R.string.retry)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    sendJSONRequest();
                                    dialog.dismiss();
                                    progressBar.dismiss();
                                }
                            })
                            .negativeText(R.string.dismiss)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    progressBar.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private ArrayList<Movie> parseJSONResponse(JSONObject response) {
//        Toast.makeText(MainActivity.this, "Page " + page, Toast.LENGTH_SHORT).show();
        if (response == null || response.length() == 0) {
            return null;
        } else try {
            JSONObject response_list_json = response.getJSONObject(Keys.DATA);
            JSONArray jsonArray = response_list_json.getJSONArray(Keys.MOVIES);
            Movie movie;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentJSONObject = jsonArray.getJSONObject(i);
                int id = currentJSONObject.getInt(Keys.ID);
                if (set.contains(id)) {
//                    Toast.makeText(MainActivity.this, "Id already exists " + id, Toast.LENGTH_SHORT).show();
                    continue;
                } else {
                    set.add(id);
                }

                String title;
                if (currentJSONObject.has(Keys.TITLE)) {
                    title = currentJSONObject.getString(Keys.TITLE);
                } else {
                    title = getString(R.string.title_absent);
                }

                String year;
                if (currentJSONObject.has(Keys.YEAR)) {
                    year = currentJSONObject.getString(Keys.YEAR);
                } else {
                    year = getString(R.string.year_absent);
                }

                String genres;
                if (currentJSONObject.has(Keys.GENRES)) {

                    JSONArray array = currentJSONObject.getJSONArray(Keys.GENRES);
                    StringBuilder genreArray = new StringBuilder();
                    for (int j = 0; j < array.length() - 1; j++) {
                        genreArray.append(array.get(j)).append(", ");
                    }
                    genreArray.append(array.get(array.length() - 1));

                    genres = genreArray.toString();

                } else {
                    genres = getString(R.string.genres_absent);
                }

                Float rating;
                if (currentJSONObject.has(Keys.RATING)) {
                    rating = (float) currentJSONObject.getDouble(Keys.RATING);
                } else {
                    rating = 0.0f;
                }
                String summary;
                if (currentJSONObject.has(Keys.SUMMARY)) {
                    summary = currentJSONObject.getString(Keys.SUMMARY);

                } else {
                    summary = getString(R.string.no_summary);
                }

                String url_movie_cover = null;
                if (currentJSONObject.has(Keys.MEDIUM_COVER_IMAGE)) {
                    url_movie_cover = currentJSONObject.getString(Keys.MEDIUM_COVER_IMAGE);
                }

                movie = new Movie(id, title, genres, year, rating, url_movie_cover);
                movie.setSummary(summary);
// Instead of fetching torrent info in the main we will fetch torrent details on demand
//                if (currentJSONObject.has(Keys.TORRENTS)) {
//                    JSONArray torrentsArray = currentJSONObject.getJSONArray(Keys.TORRENTS);
//                    JSONObject torrent;
//                    Torrent current_torrent;
//                    for (int k = 0; k < torrentsArray.length(); k++) {
//                        torrent = torrentsArray.getJSONObject(k);
//                        current_torrent = new Torrent();
//                        current_torrent.setQuality(torrent.getString(Keys.QUALITY));
//                        current_torrent.setPeers(torrent.getInt(Keys.PEERS));
//                        current_torrent.setSeeds(torrent.getInt(Keys.SEEDS));
//                        current_torrent.setSize(torrent.getString(Keys.SIZE));
//                        movie.addToTorrentList(current_torrent);
//                    }
//                }
                if (currentJSONObject.has(Keys.LARGE_COVER_IMAGE)) {
                    movie.setMovie_cover_url_large(currentJSONObject.getString(Keys.LARGE_COVER_IMAGE));
                } else {
                    movie.setMovie_cover_url_large("");
                }

                if (currentJSONObject.has(Keys.MEDIUM_COVER_IMAGE)) {
                    movie.setMovie_cover_url_medium(currentJSONObject.getString(Keys.MEDIUM_COVER_IMAGE));
                } else {
                    movie.setMovie_cover_url_large("");
                }

                if (currentJSONObject.has(Keys.YOUTUBE_TRAILER)) {
                    movie.setYouTubeurl(currentJSONObject.getString(Keys.YOUTUBE_TRAILER));
                }
                test.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return test;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this,Login.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this, Search.class));

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this,FavouriteActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String setLimit(String url, int i) {
        return url + "&limit=" + i;
    }

    private String fetchNextPage(String url) {
        return url + "&page=" + (page++);
    }

    private void loadMore(int fetch_size) {
        url = fetchNextPage(setLimit(URL, fetch_size));
        sendJSONRequest();
//        Toast.makeText(MainActivity.this,url,Toast.LENGTH_LONG).show();

    }

    private void loadMore() {
        url = setLimit(URL, 3);
        sendJSONRequest();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
      //  outState.putParcelable("list", list_movies);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.list_movies= (ArrayList<Movie>) savedInstanceState.getSerializable("movie_list");
    }
}
