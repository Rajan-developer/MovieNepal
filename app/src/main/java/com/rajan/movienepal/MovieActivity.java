package com.rajan.movienepal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rajan.movienepal.adapter.MovieRecyclerViewAdapter;
import com.rajan.movienepal.controller.movie.MovieController;
import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.utility.AppUtil;
import com.rajan.movienepal.view.IMovieView;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements IMovieView {

    //views
    RecyclerView movieRecyclerView;
    ProgressBar progressBar;
    TextView textViewErrorSpeech;

    //controller
    MovieController movieController;


    //variables


    //adapter
    MovieRecyclerViewAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        movieRecyclerView = findViewById(R.id.rv_movie);
        progressBar = findViewById(R.id.progressBar);
        textViewErrorSpeech = findViewById(R.id.textViewErrorSpeech);

        //adapter
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        //controller
        movieController = new MovieController(this);

        if (AppUtil.isInternetConnectionAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            movieController.getAllMovies();
        } else {
            movieRecyclerView.setVisibility(View.INVISIBLE);
            textViewErrorSpeech.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSucces(ArrayList<MovieModel.Result> movieList) {
        if (!movieList.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            movieAdapter = new MovieRecyclerViewAdapter(this);
            movieAdapter.addMovie(movieList);
            movieRecyclerView.setAdapter(movieAdapter);

            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_blink);
            movieRecyclerView.setLayoutAnimation(controller);
            movieAdapter.notifyDataSetChanged();
            movieRecyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onFail(String message) {
        AppUtil.showSnackBar(movieRecyclerView, this, message);
    }
}