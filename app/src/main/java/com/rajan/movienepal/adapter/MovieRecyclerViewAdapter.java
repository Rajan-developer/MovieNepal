package com.rajan.movienepal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rajan.movienepal.R;
import com.rajan.movienepal.callbacks.MovieCallBacks;
import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.utility.AppText;

import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MovieModel.Result> movieList;
    private Context mContext;
    private MovieCallBacks callBacks;

    public MovieRecyclerViewAdapter(Context mContext, MovieCallBacks callBacks) {
        this.movieList = new ArrayList<>();
        this.mContext = mContext;
        this.callBacks = callBacks;
    }

    public void addMovie(ArrayList<MovieModel.Result> list) {
        this.movieList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindMovie(movieList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout movieItem;
        RoundedImageView movieImage;
        TextView movieName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieItem = itemView.findViewById(R.id.rl_movieItem);
            movieImage = itemView.findViewById(R.id.iv_movie);
            movieName = itemView.findViewById(R.id.tv_movieName);
        }

        public void bindMovie(MovieModel.Result movie, int position) {


            Glide.with(mContext)
                    .load(AppText.IMAGE_URL + movieList.get(position).getPoster_path())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_not_found)
                    .into(movieImage);

            movieName.setText(movieList.get(position).getTitle());
            movieItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBacks.movieItemClicked(movieList.get(position));
                }
            });
        }
    }
}
