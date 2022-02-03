package com.rajan.movienepal.retrofit;

import com.rajan.movienepal.model.movie.MovieDetailModel;
import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.utility.AppText;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET(AppText.MOVIE_URL)
    Call<MovieModel> getAllMovies();

    @GET(AppText.MOVIE_DETAIL_URL + "{movie_id}" + "?api_key=80dff2970093b6849866a98cc4bf6f34")
    Call<MovieDetailModel> getMovieDetail(@Path("movie_id") String companyName);
}
