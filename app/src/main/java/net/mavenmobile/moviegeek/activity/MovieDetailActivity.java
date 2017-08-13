package net.mavenmobile.moviegeek.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.mavenmobile.moviegeek.R;
import net.mavenmobile.moviegeek.adapter.MovieReviewsAdapter;
import net.mavenmobile.moviegeek.adapter.MovieVideosAdapter;
import net.mavenmobile.moviegeek.db.MovieContract;
import net.mavenmobile.moviegeek.model.Movie;
import net.mavenmobile.moviegeek.model.MovieReviews;
import net.mavenmobile.moviegeek.model.MovieReviewsResponse;
import net.mavenmobile.moviegeek.model.MovieVideos;
import net.mavenmobile.moviegeek.model.MovieVideosResponse;
import net.mavenmobile.moviegeek.rest.ApiClient;
import net.mavenmobile.moviegeek.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements MovieVideosAdapter.OnItemClickListener {

    @BindView(R.id.ivMoviePoster)
    ImageView mIvMoviePoster;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_rating)
    TextView mTvRating;
    @BindView(R.id.tv_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_synopsis)
    TextView mTvSynopsis;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView mRecyclerViewReviews;

    private final static String API_KEY = "";
    private final static String URL_YOUTUBE_BASE = "http://www.youtube.com/watch?v=";
    @BindView(R.id.btnMarkAsFavorite)
    Button mBtnMarkAsFavorite;
    private ApiInterface apiService;
    private String TAG = MovieDetailActivity.class.getSimpleName();
    private Movie movieDetail;
    private List<MovieVideos> mMovieVideos;
    private MovieVideosAdapter mMovieVideosAdapter;
    private List<MovieReviews> mMovieReviews;
    private MovieReviewsAdapter mMovieReviewsAdapter;
    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (API_KEY.isEmpty()) {
            Toast.makeText(this, "Insert your API KEY", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMovieId = bundle.getInt("movieId");
        }

        loadMovieDetails();
        loadValueMovieVideos();
        loadValueMovieReviews();

        mBtnMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "movieId=?", new String[]{String.valueOf(movieDetail.getId())}, null);
                if (cursor.moveToFirst()) {
                    String movieFavorite = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IS_FAVOURITE));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("movieFavorite", movieFavorite == "1" ? "0" : "1");
                    getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, contentValues, "movieId=?", new String[]{String.valueOf(movieDetail.getId())});
                    Toast.makeText(MovieDetailActivity.this, "Mark movie as favorite", Toast.LENGTH_SHORT).show();

                } else {
                    String movieFavorite = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IS_FAVOURITE));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("movieFavorite", movieFavorite == "0" ? "1" : "0");
                    getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, contentValues, "movieId=?", new String[]{String.valueOf(movieDetail.getId())});
                    Toast.makeText(MovieDetailActivity.this, "Remove movie from favorite", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void loadMovieDetails() {
        Call<Movie> call = apiService.getMovieDetails(mMovieId, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieDetail = response.body();
                    setupValueMovieDetail(movieDetail);
                } else {
                    Log.e(TAG, "Error to get detail");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setupValueMovieDetail(Movie movieDetail) {
        this.movieDetail = movieDetail;

        Glide.with(this)
                .load(movieDetail.getPosterPath())
                .override(185, 278)
                .into(mIvMoviePoster);
        mIvMoviePoster.setAdjustViewBounds(true);

        mTvTitle.setText(movieDetail.getTitle());
        mTvReleaseDate.setText(movieDetail.getReleaseDate());
        mTvRating.setText(movieDetail.getVoteAverage() + "/10");
        mTvSynopsis.setText(movieDetail.getOverview());
    }

    private void loadValueMovieVideos() {
        Call<MovieVideosResponse> call = apiService.getMovieVideos(mMovieId, API_KEY);
        call.enqueue(new Callback<MovieVideosResponse>() {
            @Override
            public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
                if (response.isSuccessful()) {
                    mMovieVideos = response.body().getResults();
                    Log.d(TAG, "Number of movies: " + mMovieVideos.size());
                    setupAdapter();
                } else {
                    Log.e(TAG, "Error to get movie videos");
                }
            }

            @Override
            public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setupAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMovieVideosAdapter = new MovieVideosAdapter(mMovieVideos, this, this);
        mRecyclerView.setAdapter(mMovieVideosAdapter);
    }

    @Override
    public void onItemClick(int position, String movieUrl) {
//        movieUrl = mMovieVideos.get(position).getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_YOUTUBE_BASE + movieUrl));
        startActivity(intent);
    }

    private void loadValueMovieReviews() {
        Call<MovieReviewsResponse> call = apiService.getMovieReviews(mMovieId, API_KEY);
        call.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                if (response.isSuccessful()) {
                    mMovieReviews = response.body().getResults();
                    Log.d(TAG, "Number of movies: " + mMovieReviews.size());
                    setupReviewsAdapter();
                } else {
                    Log.e(TAG, "Error to get movie videos");
                }
            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setupReviewsAdapter() {
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMovieReviewsAdapter = new MovieReviewsAdapter(mMovieReviews, this);
        mRecyclerViewReviews.setAdapter(mMovieReviewsAdapter);
    }
}