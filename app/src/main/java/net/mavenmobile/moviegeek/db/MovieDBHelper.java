package net.mavenmobile.moviegeek.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.mavenmobile.moviegeek.model.Movie;

/**
 * Created by bembengcs on 8/12/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieList.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addMovieTable(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addMovieTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry.COLUMN_ID + " INTEGER, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_RATING + " REAL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_IS_TOP_RATED + " INTEGER, " +
                        MovieContract.MovieEntry.COLUMN_IS_POPULAR + " INTEGER, " +
                        MovieContract.MovieEntry.COLUMN_IS_FAVOURITE + " INTEGER " + " );"
        );
    }
}
