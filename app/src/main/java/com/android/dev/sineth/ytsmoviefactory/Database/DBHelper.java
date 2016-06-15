package com.android.dev.sineth.ytsmoviefactory.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.dev.sineth.ytsmoviefactory.Resources.Keys;

/**
 * Created by Sineth on 2/25/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String MOVIE = "Movie";
    public static final String DATABASE_NAME = "MovieDetails.db";
    public static final int DATABASE_VERSION = 1;
    public static final String FAVOURITE = "favourite";
    /**
     * This string is used to create new table
     */
    private static final String CREATE_TABLE_CARDS = "CREATE TABLE " + MOVIE + " ( " + Keys.ID +
            " VARCHAR(30) PRIMARY KEY, " + Keys.TITLE + " VARCHAR(30), " + Keys.YEAR + " INTEGER(4), " +
            Keys.GENRES + " VARCHAR(60)," + Keys.RATING + " REAL," + Keys.SUMMARY + " VARCHAR(2000)," +
            Keys.LARGE_COVER_IMAGE + " VARCHAR(2083), " + Keys.MEDIUM_COVER_IMAGE + " VARCHAR(2083), " + Keys.SMALL_COVER_IMAGE + " VARCHAR(2083), " +
            Keys.YOUTUBE_TRAILER + " VARCHAR(50)" +
            ");";

    private static final String CREATE_TABLE_FAVOURITE = "CREATE TABLE " + FAVOURITE + " ( " + Keys.ID + " VARCHAR(30) PRIMARY KEY, " +
            Keys.ISFAV + " INTEGER(2) " + " ); ";
    private static final String DROP_TABLE_TASKS = "DROP TABLE IF EXISTS" + MOVIE;
    private Context context;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("TAG", "creating the database");
        db.execSQL(CREATE_TABLE_CARDS);
        db.execSQL(CREATE_TABLE_FAVOURITE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_TASKS);
        onCreate(db);
    }

    /**
     * this function returns a writable instance of the database
     *
     * @return SQLiteDatabase
     */
    @Override
    public SQLiteDatabase getWritableDatabase() {

        return super.getWritableDatabase();

    }
}


