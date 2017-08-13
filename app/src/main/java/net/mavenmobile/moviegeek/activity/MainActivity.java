package net.mavenmobile.moviegeek.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.mavenmobile.moviegeek.R;
import net.mavenmobile.moviegeek.adapter.MoviesAdapter;
import net.mavenmobile.moviegeek.db.MovieContract;
import net.mavenmobile.moviegeek.model.Movie;
import net.mavenmobile.moviegeek.model.MoviesResponse;
import net.mavenmobile.moviegeek.rest.ApiClient;
import net.mavenmobile.moviegeek.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private final static String API_KEY = "";
    private String TAG = MainActivity.class.getSimpleName();
    private List<Movie> movieList;
    private ApiInterface apiService;
    private MoviesAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (API_KEY.isEmpty()) {
            Toast.makeText(this, "Insert your API KEY", Toast.LENGTH_SHORT).show();
            return;
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        loadTopRatedMovie();
        loadPopularMovie();
        //default adapter
        setupAdapter(true, false, false);
    }

    private void insertPopularToDB(List<Movie> movies) {
        for (Movie movie : movies) {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "movieId=?", new String[]{String.valueOf(movie.getId())}, null);
            if (!cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
                contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
                contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
                contentValues.put(MovieContract.MovieEntry.COLUMN_IS_POPULAR, 1);

                getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            }
        }
    }

    private void insertTopRatedToDB(List<Movie> movies) {
        for (Movie movie : movies) {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "movieId=?", new String[]{String.valueOf(movie.getId())}, null);
            if (!cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
                contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
                contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
                contentValues.put(MovieContract.MovieEntry.COLUMN_IS_TOP_RATED, 1);

                getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            }
        }
    }

    private void loadTopRatedMovie() {
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    movieList = response.body().getResults();
                    insertTopRatedToDB(movieList);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void loadPopularMovie() {
        Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    movieList = response.body().getResults();
                    insertPopularToDB(movieList);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setupAdapter(boolean isPopular, boolean isTopRated, boolean isFavorite) {
        mMovieAdapter = new MoviesAdapter(MainActivity.this, this, isPopular, isTopRated, isFavorite);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_top_rated_movies) {
            setupAdapter(true, false, false);
            return true;
        } else if (id == R.id.action_popular_movies) {
            setupAdapter(false, true, false);
            return true;
        } else if (id == R.id.action_favourite_movies) {
            setupAdapter(false, false, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position, int movieId) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

    }
}
