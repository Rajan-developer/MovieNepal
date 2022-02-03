package com.rajan.movienepal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajan.movienepal.R;
import com.rajan.movienepal.model.movie.MovieModel;

import java.util.ArrayList;
public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MovieModel.Result> movieList;
    private Context mContext;

    public MovieRecyclerViewAdapter(Context mContext) {
        this.movieList = new ArrayList<>();
        this.mContext = mContext;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindMovie(MovieModel.Result movie, int position) {

        }
    }
}
