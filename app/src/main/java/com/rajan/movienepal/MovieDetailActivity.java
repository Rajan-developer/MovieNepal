package com.rajan.movienepal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajan.movienepal.controller.movie.MovieController;
import com.rajan.movienepal.model.movie.MovieDetailModel;
import com.rajan.movienepal.utility.AppUtil;
import com.rajan.movienepal.view.IMovieDetailView;

public class MovieDetailActivity extends AppCompatActivity implements IMovieDetailView {

    //variables
    final public static String MOVIE_ID = "movie_id";
    Intent intent;
    String movieId;

    //view
    ImageView movieImage;
    TextView movieName, movieGeneres, movieReleaseDate, movieTimeInterval, movieRating, movieOverView, movieCollection, movieProductionCompany, movieProductionCountry;
    RecyclerView movieImageRecyclerView;
    RelativeLayout progressBar;
    RelativeLayout noInternet;

    //controller
    MovieController movieController;

    //Adapter


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get movie id
        intent = getIntent();
        movieId = intent.getStringExtra(MOVIE_ID);
        Toast.makeText(this, movieId, Toast.LENGTH_SHORT).show();

        //initializing views
        intiView();

        //controller
        movieController = new MovieController(this);


        //adapter
        movieImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //controller
        movieController = new MovieController(this);

        if (AppUtil.isInternetConnectionAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            movieController.getMovieDetail(movieId);
        } else {
            movieImageRecyclerView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
        }


    }

    public void intiView() {
        movieImage = findViewById(R.id.iv_detail_movie);
        movieName = findViewById(R.id.tv_movieName);
        movieGeneres = findViewById(R.id.tv_movieGeneres);
        movieReleaseDate = findViewById(R.id.tv_movieReleaseDate);
        movieTimeInterval = findViewById(R.id.tv_movieTimeInterval);
        movieRating = findViewById(R.id.tv_ratingValue);
        movieOverView = findViewById(R.id.tv_movieOverView);
        movieCollection = findViewById(R.id.tv_movieCollection);
        movieProductionCompany = findViewById(R.id.tv_productionCompany);
        movieProductionCountry = findViewById(R.id.tv_productionCountry);
        movieImageRecyclerView = findViewById(R.id.rv_image_related);

        progressBar = findViewById(R.id.rl_progressBar);
        noInternet = findViewById(R.id.rl_no_internet);
    }

    @Override
    public void onSucces(MovieDetailModel movieDetail) {
        //set values in the UI of movie
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, movieDetail.getBelongs_to_collection().getName(), Toast.LENGTH_SHORT).show();
        movieName.setText(movieDetail.getTitle());

        //get multiple generas from list
        String genre = "";
        for(int i = 0; i< movieDetail.getGenres().size(); i++){
            genre = genre + movieDetail.getGenres().get(i).getName() + ", ";
        }
        movieGeneres.setText(genre);

        AppUtil.DateConverter(movieDetail.getRelease_date());
    }

    @Override
    public void onFail(String message) {

    }


}
