package com.rajan.movienepal.view;

import com.rajan.movienepal.model.movie.MovieDetailModel;

public interface IMovieDetailView {

    void onSucces(MovieDetailModel movieDetail);

    void onFail(String message);
}
