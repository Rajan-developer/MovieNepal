package com.rajan.movienepal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rajan.movienepal.R;
import com.rajan.movienepal.utility.AppText;

import java.util.ArrayList;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> imageList;
    private Context mContext;

    public ImageRecyclerViewAdapter(Context mContext) {
        this.imageList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void addMovie(ArrayList<String> list) {
        this.imageList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindMovie(imageList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView movieImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.iv_movie_related);
        }

        public void bindMovie(String imagePath, int position) {

            Glide.with(mContext)
                    .load(AppText.IMAGE_URL + imageList.get(position))
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_not_found)
                    .into(movieImage);

        }
    }
}
