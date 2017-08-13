package net.mavenmobile.moviegeek.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.mavenmobile.moviegeek.R;
import net.mavenmobile.moviegeek.db.MovieContract;
import net.mavenmobile.moviegeek.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bembengcs on 8/3/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<net.mavenmobile.moviegeek.model.Movie> mMovies;
    private Context mContext;
    private OnItemClickListener mListener;

    public MoviesAdapter(Context context, OnItemClickListener listener, boolean isPopular, boolean isTopRated, boolean isFavourite) {
        this.mMovies = new ArrayList<>();
        this.mContext = context;
        this.mListener = listener;
        Cursor cursor = null;

        if (isPopular)
        cursor = context.getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI, null, "moviePopular=?", new String[]{String.valueOf(1)}, null);
        else if (isTopRated)
            cursor = context.getContentResolver()
                    .query(MovieContract.MovieEntry.CONTENT_URI, null, "movieTopRated=?", new String[]{String.valueOf(1)}, null);
        else if (isFavourite)
            cursor = context.getContentResolver()
                    .query(MovieContract.MovieEntry.CONTENT_URI, null, "movieFavorite=?", new String[]{String.valueOf(1)}, null);

        while (cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
            String TITLE = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String RELEASE_DATE = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            String POSTER_PATH = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
            Double RATING = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
            String SYNOPSIS = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));

            Movie movie = new Movie();
            movie.setId(ID);
            movie.setTitle(TITLE);
            movie.setReleaseDate(RELEASE_DATE);
            movie.setPosterPath(POSTER_PATH);
            movie.setVoteAverage(RATING);
            movie.setOverview(SYNOPSIS);

            this.mMovies.add(movie);
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivMoviePoster)
        ImageView mIvMoviePoster;
        @BindView(R.id.movies_layout)
        FrameLayout mMoviesLayout;

        private net.mavenmobile.moviegeek.model.Movie movie;
        private int position;

        @OnClick(R.id.ivMoviePoster)
        public void onViewClicked() {
            mListener.onItemClick(position, movie.getId());
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(net.mavenmobile.moviegeek.model.Movie movie, int position) {
            this.movie = movie;
            this.position = position;

            Glide.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185"+movie.getPosterPath())
                    .override(185, 278)
                    .into(mIvMoviePoster);
            mIvMoviePoster.setAdjustViewBounds(true);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mMovies.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int movieId);
    }
}
