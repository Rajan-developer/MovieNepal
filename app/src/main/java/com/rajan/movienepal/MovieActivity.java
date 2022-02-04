package com.rajan.movienepal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rajan.movienepal.adapter.MovieRecyclerViewAdapter;
import com.rajan.movienepal.callbacks.MovieCallBacks;
import com.rajan.movienepal.controller.movie.MovieController;
import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.utility.AppUtil;
import com.rajan.movienepal.view.IMovieView;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements IMovieView {

    //views
    RecyclerView movieRecyclerView;
    RelativeLayout progressBar;
    RelativeLayout noInternet;

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
        progressBar = findViewById(R.id.rl_progressBar);
        noInternet = findViewById(R.id.rl_no_internet);

        //adapter
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        //controller
        movieController = new MovieController(this, MovieActivity.this);

        if (AppUtil.isInternetConnectionAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            movieController.getAllMovies();
        } else {
            movieRecyclerView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            movieController.getAllMoviesFromDataBase();
        }
    }

    @Override
    public void onSucces(ArrayList<MovieModel.Result> movieList) {
        if (!movieList.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            movieAdapter = new MovieRecyclerViewAdapter(this, new MovieCallBacks() {
                @Override
                public void movieItemClicked(MovieModel.Result movie) {
                    Intent detailIntent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                    detailIntent.putExtra(MovieDetailActivity.MOVIE_ID, String.valueOf(movie.getId()));
                    startActivity(detailIntent);
                    overridePendingTransition(R.anim.slide_right,R.anim.no_animation);
                }
            });
            movieAdapter.addMovie(movieList);
            movieRecyclerView.setAdapter(movieAdapter);

            //blinking animation for item loading in recycler view
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_blink);
            movieRecyclerView.setLayoutAnimation(controller);
            movieAdapter.notifyDataSetChanged();
            movieRecyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onOffLineDataSuccess(ArrayList<MovieModel.Result> movieList) {
        if (!movieList.isEmpty()) {
            AppUtil.showSnackBar(movieRecyclerView,this,getString(R.string.off_line_mode));
            movieRecyclerView.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            movieAdapter = new MovieRecyclerViewAdapter(this, new MovieCallBacks() {
                @Override
                public void movieItemClicked(MovieModel.Result movie) {
                    Intent detailIntent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                    detailIntent.putExtra(MovieDetailActivity.MOVIE_ID, String.valueOf(movie.getId()));
                    startActivity(detailIntent);
                }
            });
            movieAdapter.addMovie(movieList);
            movieRecyclerView.setAdapter(movieAdapter);

            //blinking animation for item loading in recycler view
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