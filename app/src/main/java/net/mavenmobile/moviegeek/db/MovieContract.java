package net.mavenmobile.moviegeek.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bembengcs on 8/12/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "net.mavenmobile.moviegeek";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static  final String PATH_MOVIE = "movie";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_ID = "movieId";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_POSTER_PATH = "moviePosterPath";
        public static final String COLUMN_RATING = "movieRating";
        public static final String COLUMN_SYNOPSIS = "movieSynopsis";
        public static final String COLUMN_IS_POPULAR = "moviePopular";
        public static final String COLUMN_IS_TOP_RATED = "movieTopRated";
        public static final String COLUMN_IS_FAVOURITE = "movieFavorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
