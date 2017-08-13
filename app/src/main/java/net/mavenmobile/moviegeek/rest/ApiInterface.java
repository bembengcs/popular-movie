package net.mavenmobile.moviegeek.rest;

import net.mavenmobile.moviegeek.model.Movie;
import net.mavenmobile.moviegeek.model.MovieReviewsResponse;
import net.mavenmobile.moviegeek.model.MovieVideosResponse;
import net.mavenmobile.moviegeek.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bembengcs on 8/3/2017.
 */

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieVideosResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

}
