package com.rajan.movienepal;

import android.content.Intent;
import android.media.Image;
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

import com.bumptech.glide.Glide;
import com.rajan.movienepal.adapter.ImageRecyclerViewAdapter;
import com.rajan.movienepal.controller.movie.MovieController;
import com.rajan.movienepal.database.entity.Movie;
import com.rajan.movienepal.model.movie.MovieDetailModel;
import com.rajan.movienepal.utility.AppText;
import com.rajan.movienepal.utility.AppUtil;
import com.rajan.movienepal.view.IMovieDetailView;

import java.util.ArrayList;

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
    ImageRecyclerViewAdapter imageRecyclerViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get movie id
        intent = getIntent();
        movieId = intent.getStringExtra(MOVIE_ID);

        //initializing views
        intiView();

        //controller
        movieController = new MovieController(this, MovieDetailActivity.this);


        //adapter
        movieImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        if (AppUtil.isInternetConnectionAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            movieController.getMovieDetail(movieId);
        } else {
            movieImageRecyclerView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            movieController.getMovieFromDataBase(movieId);
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

        ArrayList<String> imagePaths = new ArrayList<>();
        imagePaths.add(movieDetail.getBackdrop_path());
        imagePaths.add(movieDetail.getPoster_path());


        movieName.setText(movieDetail.getTitle());
        Glide.with(this)
                .load(AppText.IMAGE_URL + movieDetail.getPoster_path())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_not_found)
                .into(movieImage);


        //get multiple generas from list
        String genre = "";
        for (int i = 0; i < movieDetail.getGenres().size(); i++) {
            genre = genre + movieDetail.getGenres().get(i).getName() + ", ";
        }
        movieGeneres.setText(genre);
        movieReleaseDate.setText(AppUtil.DateConverter(movieDetail.getRelease_date()));
        movieTimeInterval.setText(AppUtil.timeConverter(movieDetail.getRuntime()));
        movieRating.setText(String.valueOf(movieDetail.getVote_average()));
        movieOverView.setText(movieDetail.getOverview());

        String belongsToCollection = "";
        try {
            belongsToCollection = movieDetail.getBelongs_to_collection().getName();
            movieCollection.setText(belongsToCollection);
            imagePaths.add(movieDetail.getBelongs_to_collection().getPoster_path());
            imagePaths.add(movieDetail.getBelongs_to_collection().getBackdrop_path());
        } catch (NullPointerException e) {
            e.printStackTrace();
            movieCollection.setText("No collection");
        }


        String productionCompany = "";
        for (int i = 0; i < movieDetail.getProduction_companies().size(); i++) {
            productionCompany = productionCompany + movieDetail.getProduction_companies().get(i).getName() + "\n";
        }
        movieProductionCompany.setText(productionCompany);


        String productionCountry = "";
        for (int i = 0; i < movieDetail.getProduction_countries().size(); i++) {
            productionCountry = productionCountry + movieDetail.getProduction_countries().get(i).getName() + "\n";
        }
        movieProductionCountry.setText(productionCountry);

        //related images of the movie
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(this);
        imageRecyclerViewAdapter.addImages(imagePaths);
        movieImageRecyclerView.setAdapter(imageRecyclerViewAdapter);

        //update the value on table when online
        movieController.UpdateMovieToDataBase(
                movieId,
                genre,
                AppUtil.DateConverter(movieDetail.getRelease_date()),
                AppUtil.timeConverter(movieDetail.getRuntime()),
                belongsToCollection,
                productionCompany,
                productionCountry,
                movieDetail.getHomepage()
        );

    }

    @Override
    public void onOffLineDataSuccess(Movie movie) {

        if (movie.getStatus()) {
            AppUtil.showSnackBar(movieImageRecyclerView, this, getString(R.string.off_line_mode));
            noInternet.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }

        movieName.setText(movie.getTitle());
        /*Glide.with(this)
                .load(AppText.IMAGE_URL + movieDetail.getPoster_path())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_not_found)
                .into(movieImage);*/


        movieGeneres.setText(movie.getGenres());
        movieReleaseDate.setText(movie.getRelease_date());
        movieTimeInterval.setText(movie.getDuration());
        movieRating.setText(String.valueOf(movie.getVote_average()));
        movieOverView.setText(movie.getOverview());
        movieCollection.setText(movie.getBelongs_to_collection());
        movieProductionCompany.setText(movie.getProduction_companies());
        movieProductionCountry.setText(movie.getProduction_countries());
    }

    @Override
    public void onFail(String message) {

    }


}
