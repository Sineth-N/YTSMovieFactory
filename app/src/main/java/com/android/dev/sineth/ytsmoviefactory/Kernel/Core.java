package com.android.dev.sineth.ytsmoviefactory.Kernel;

import com.android.dev.sineth.ytsmoviefactory.Models.Movie;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

/**
 * Created by Sineth on 4/4/2016.
 */
public class Core {
    private static volatile List<Movie> movies = new Vector<>();
    private static HashSet<Integer> movieIDHashSet = new HashSet<>();
    private static String baseURL = "https://yts.ag/api/v2/list_movies.json?";

    public static HashSet<Integer> getMovieIDHashSet() {
        return movieIDHashSet;
    }

    public static String getBaseURL() {
        return baseURL;
    }

    public static List<Movie> getMovies() {
        return movies;
    }
}
