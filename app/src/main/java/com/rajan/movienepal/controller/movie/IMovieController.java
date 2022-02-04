package com.rajan.movienepal.controller.movie;

import com.rajan.movienepal.model.movie.MovieModel;

import java.util.ArrayList;

public interface IMovieController {

    void getAllMovies();

    void getMovieDetail(String movieId);

    void getAllMoviesFromDataBase();

    void getMovieFromDataBase(String id);

    void InsertMoviesToDataBase(ArrayList<MovieModel.Result> movieList);

    void UpdateMovieToDataBase(String movieId, String genre, String releasedDate, String duration,
                               String collection, String prodCompany, String prodCountry, String posterPath,String backDropPath, String homePageLink);
}
