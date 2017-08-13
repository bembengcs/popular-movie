package net.mavenmobile.moviegeek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavenmobile.moviegeek.R;
import net.mavenmobile.moviegeek.model.MovieReviews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bembengcs on 8/6/2017.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MyViewHolder> {

    private List<MovieReviews> mMovieReviewsList;
    private Context mContext;

    public MovieReviewsAdapter(List<MovieReviews> movieReviewsList, Context context) {
        this.mMovieReviewsList = movieReviewsList;
        this.mContext = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author_name)
        TextView mTvAuthorName;
        @BindView(R.id.tv_content)
        TextView mTvContent;

        private MovieReviews movieReviews;
        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MovieReviews movieReviews, int position) {
            this.movieReviews = movieReviews;
            this.position = position;

            mTvAuthorName.setText(movieReviews.getAuthor());
            mTvContent.setText(movieReviews.getContent());

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mMovieReviewsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMovieReviewsList.size();
    }

}
