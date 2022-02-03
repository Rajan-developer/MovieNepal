package com.rajan.movienepal.retrofit;

import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.utility.AppText;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET(AppText.MOVIE_URL)
    Call<MovieModel> getAllMovies();
}
