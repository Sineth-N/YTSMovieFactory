package com.android.dev.sineth.ytsmoviefactory.Kernel;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.Models.Torrent;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.Resources.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sineth on 4/4/2016.
 */
public class JSONParser {

    /**
     *parses the JSON feed for the list of movies and adds the details into a ArrayList
     * of movies.
     * @param response
     * @param context
     */
    public static void parseJSONResponse(JSONObject response, Context context) {

        if (response == null || response.length() == 0) {
            return;
        } else try {
            JSONObject response_list_json = response.getJSONObject(Keys.DATA);
            JSONArray jsonArray = response_list_json.getJSONArray(Keys.MOVIES);
            Movie movie;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentJSONObject = jsonArray.getJSONObject(i);
                int id = currentJSONObject.getInt(Keys.ID);
                if (Core.getMovieIDHashSet().contains(id)) {
//                    Toast.makeText(MainActivity.this, "Id already exists " + id, Toast.LENGTH_SHORT).show();
                    continue;
                } else {
                    Core.getMovieIDHashSet().add(id);
                }

                String title;
                if (currentJSONObject.has(Keys.TITLE)) {
                    title = currentJSONObject.getString(Keys.TITLE);
                } else {
                    title = context.getString(R.string.title_absent);
                }

                String year;
                if (currentJSONObject.has(Keys.YEAR)) {
                    year = currentJSONObject.getString(Keys.YEAR);
                } else {
                    year = context.getString(R.string.year_absent);
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
                    genres = context.getString(R.string.genres_absent);
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
                    summary = context.getString(R.string.no_summary);
                }

                String url_movie_cover = null;
                if (currentJSONObject.has(Keys.MEDIUM_COVER_IMAGE)) {
                    url_movie_cover = currentJSONObject.getString(Keys.MEDIUM_COVER_IMAGE);
                }

                movie = new Movie(id, title, genres, year, rating, url_movie_cover);
                movie.setSummary(summary);
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
                Core.getMovies().add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Nullable
    public static List<Torrent> parseJSONResponseTorrent(JSONObject response, Context context) {
        if (response.has(Keys.TORRENTS)) {
            JSONArray torrentsArray = null;
            try {
                torrentsArray = response.getJSONArray(Keys.TORRENTS);
                JSONObject torrent;
                Torrent current_torrent;
                List<Torrent> torrentArrayList=new ArrayList<>();
                for (int k = 0; k < torrentsArray.length(); k++) {
                    torrent = torrentsArray.getJSONObject(k);
                    current_torrent = new Torrent();
                    current_torrent.setQuality(torrent.getString(Keys.QUALITY));
                    current_torrent.setPeers(torrent.getInt(Keys.PEERS));
                    current_torrent.setSeeds(torrent.getInt(Keys.SEEDS));
                    current_torrent.setSize(torrent.getString(Keys.SIZE));
                    torrentArrayList.add(current_torrent);
                }
                return torrentArrayList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
