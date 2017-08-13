package net.mavenmobile.moviegeek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mavenmobile.moviegeek.R;
import net.mavenmobile.moviegeek.model.MovieVideos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bembengcs on 8/6/2017.
 */

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MyViewHolder> {


    private List<MovieVideos> mMovieVideosList;
    private Context mContext;
    private OnItemClickListener mListener;

    public MovieVideosAdapter(List<MovieVideos> movieVideosList, Context context, OnItemClickListener listener) {
        mMovieVideosList = movieVideosList;
        mContext = context;
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_trailer_name)
        TextView mTvTrailerName;
        @BindView(R.id.btnPlayTrailers)
        LinearLayout mBtnPlayTrailers;

        private MovieVideos movieVideos;
        private int position;

        @OnClick(R.id.btnPlayTrailers)
        public void onViewClicked() {
            mListener.onItemClick(position, movieVideos.getKey());
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MovieVideos movieVideos, int position) {
            this.movieVideos = movieVideos;
            this.position = position;

            mTvTrailerName.setText(movieVideos.getName());

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailers, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mMovieVideosList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMovieVideosList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String movieUrl);
    }
}
