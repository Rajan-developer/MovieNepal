package com.rajan.movienepal.view;

import com.rajan.movienepal.database.entity.Movie;
import com.rajan.movienepal.model.movie.MovieDetailModel;
import com.rajan.movienepal.model.movie.MovieModel;

import java.util.ArrayList;

public interface IMovieDetailView {

    void onSucces(MovieDetailModel movieDetail);

    void onFail(String message);

    void onOffLineDataSuccess(Movie movie);
}
