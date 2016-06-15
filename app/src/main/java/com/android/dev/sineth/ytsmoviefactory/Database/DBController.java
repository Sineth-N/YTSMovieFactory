package com.android.dev.sineth.ytsmoviefactory.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.Resources.Keys;

import java.util.ArrayList;

/**
 * Created by Sineth on 6/14/2016.
 */

public class DBController {

    public static boolean insertMovie(Context context, Movie movie) {
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues valuesFav = new ContentValues();
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
//        valuesFav.put(Keys.ISFAV,1);
        long rowCount = sqLiteDatabase.insert(DBHelper.MOVIE, null, values);
        return rowCount != -1;
    }

    public static boolean insertFavourite(Context context, int movieId) {
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Keys.ID, movieId);
        contentValues.put(Keys.ISFAV, 1);
        long rowCount = sqLiteDatabase.insert(DBHelper.FAVOURITE, null, contentValues);
        return rowCount != -1;
    }

    public static Boolean getFavourite(Context context, int movieId) {
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBHelper.FAVOURITE, new String[]{Keys.ISFAV}, Keys.ID + "=?", new String[]{String.valueOf(movieId)}, null, null, null);

        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(Keys.ISFAV)).equals("1");
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();

        }
        return false;

    }

    public static boolean removeFavourite(Context context, int id) {
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        int cursor = sqLiteDatabase.delete(DBHelper.FAVOURITE, Keys.ID + "=?", new String[]{String.valueOf(id)});

        if (cursor != -1) {
            return true;
        }
        return false;

    }

    public static boolean removeMovie(Context context, int id) {
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        int cursor = sqLiteDatabase.delete(DBHelper.MOVIE, Keys.ID + "=?", new String[]{String.valueOf(id)});

        if (cursor != -1) {
            return true;
        }
        return false;

    }

    public static ArrayList<Movie> getMovieAll(Context context) {
        ArrayList<Movie> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBHelper.MOVIE, null, null, null, null, null, null);
        Movie movie;
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(Keys.ID));
            String title = cursor.getString(cursor.getColumnIndex(Keys.TITLE));
            String year = cursor.getString(cursor.getColumnIndex(Keys.YEAR));
            String genre = cursor.getString(cursor.getColumnIndex(Keys.GENRES));
            String summary = cursor.getString(cursor.getColumnIndex(Keys.SUMMARY));
            String largePoster = cursor.getString(cursor.getColumnIndex(Keys.LARGE_COVER_IMAGE));
            String mediumPoster = cursor.getString(cursor.getColumnIndex(Keys.MEDIUM_COVER_IMAGE));
            String yt = cursor.getString(cursor.getColumnIndex(Keys.YOUTUBE_TRAILER));
            float rating = cursor.getFloat(cursor.getColumnIndex(Keys.RATING));
            movie=new Movie(id,title,genre,year,rating,mediumPoster);
            movie.setSummary(summary);
            movie.setMovie_cover_url_large(largePoster);
            movie.setYouTubeurl(yt);
            list.add(movie);
        }
        return list;
    }
}
