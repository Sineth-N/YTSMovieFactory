package com.android.dev.sineth.ytsmoviefactory.Kernel;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.View.YTSApplication;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

/**
 * Created by Sineth on 4/4/2016.
 */
public class Core {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String NAME = "nameKey";
    public static final String PHONE = "phoneKey";
    public static final String EMAIL = "emailKey";
    public static volatile String token = "";

    private static volatile List<Movie> movies = new Vector<>();
    private static HashSet<Integer> movieIDHashSet = new HashSet<>();
    private static String baseURL = "https://yts.ag/api/v2/list_movies.json?";
    private static boolean loggedIn;
    private static String code = "07e9327d";
    public static String loginURL = "http://" + code + ".ngrok.io/api/auth/login?";
    public static String signUpURL = "http://" + code + ".ngrok.io/api/auth/signup?";

    public static HashSet<Integer> getMovieIDHashSet() {
        return movieIDHashSet;
    }

    public static String getBaseURL() {
        return baseURL;
    }

    public static List<Movie> getMovies() {
        return movies;
    }

    public static boolean isLoggedIn() {

        return loggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        Core.loggedIn = loggedIn;
    }

    public void setLoginCredentials(String name, String email, int phone) {
        SharedPreferences sharedPreferences = YTSApplication.getAppContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putInt(PHONE, phone);
        editor.commit();
    }

    public void logout() {
        SharedPreferences sharedPreferences = YTSApplication.getAppContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, "");
        editor.putString(EMAIL, "");
        editor.putInt(PHONE, 0);
        editor.commit();
    }
}
