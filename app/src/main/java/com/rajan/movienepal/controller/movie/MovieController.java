package com.rajan.movienepal.controller.movie;

import android.util.Log;

import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.retrofit.ApiClient;
import com.rajan.movienepal.retrofit.ApiInterface;
import com.rajan.movienepal.utility.AppLog;
import com.rajan.movienepal.utility.ErrorUtils;
import com.rajan.movienepal.view.IMovieView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieController implements IMovieController {

    private IMovieView movieView;

    public MovieController(IMovieView movieView) {
        this.movieView = movieView;
    }

    @Override
    public void getAllMovies() {
        FetchMoviesFromAPI();
    }


    /* method to call and api and return the movie list*/
    void FetchMoviesFromAPI() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getAllMovies().enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                MovieModel movie = null;
                String s = null;


                if (response.isSuccessful()) {


                    AppLog.showLog("RESPONSE ", response.toString());
                    AppLog.showLog("RESPONSE ", String.valueOf(response.body().getPage()));
                    AppLog.showLog("RESPONSE ", String.valueOf(response.body().getResultList().size()));
                    movieView.onSucces(response.body().getResultList());

                } else if (response.code() == 401) {
                    movieView.onFail(ErrorUtils.parseError(response).getMsg());
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("TAG", "error on fetching movie : " + t.getMessage());
            }
        });
    }
}
