package com.rajan.movienepal.view;

import com.rajan.movienepal.model.movie.MovieModel;

import java.util.ArrayList;

public interface IMovieView {

    void onSucces(ArrayList<MovieModel.Result> movieList);

    void onFail(String message);

    void onOffLineDataSuccess(ArrayList<MovieModel.Result> movieList);
}
