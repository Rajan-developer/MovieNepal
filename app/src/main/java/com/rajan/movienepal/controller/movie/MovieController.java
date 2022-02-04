package com.rajan.movienepal.controller.movie;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.rajan.movienepal.database.DatabaseClient;
import com.rajan.movienepal.database.entity.Movie;
import com.rajan.movienepal.model.movie.MovieDetailModel;
import com.rajan.movienepal.model.movie.MovieModel;
import com.rajan.movienepal.retrofit.ApiClient;
import com.rajan.movienepal.retrofit.ApiInterface;
import com.rajan.movienepal.utility.AppLog;
import com.rajan.movienepal.utility.ErrorUtils;
import com.rajan.movienepal.view.IMovieDetailView;
import com.rajan.movienepal.view.IMovieView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieController implements IMovieController {

    private IMovieView movieView;
    private IMovieDetailView movieDetailView;
    private Context mContext;

    public MovieController(IMovieView movieView, Context context) {
        this.movieView = movieView;
        this.mContext = context;
    }

    public MovieController(IMovieDetailView movieDetailView, Context context) {
        this.movieDetailView = movieDetailView;
        this.mContext = context;
    }

    @Override
    public void getAllMovies() {
        FetchMoviesFromAPI();
    }

    @Override
    public void getMovieDetail(String movieId) {
        FetchMovieDetailFromAPI(movieId);
    }

    @Override
    public void getAllMoviesFromDataBase() {

        class GetAllMovies extends AsyncTask<Void, Void, ArrayList<MovieModel.Result>> {

            @Override
            protected ArrayList<MovieModel.Result> doInBackground(Void... voids) {
                List<Movie> movieList = DatabaseClient
                        .getInstance(mContext)
                        .getAppDatabase()
                        .movieDAO()
                        .getAll();

                ArrayList<MovieModel.Result> offLineMovies = new ArrayList<>();
                for (int i = 0; i < movieList.size(); i++) {
                    //creating a movie
                    MovieModel.Result movie = new MovieModel.Result();
                    movie.setId(Long.valueOf(movieList.get(i).getMovieId()));
                    movie.setTitle(movieList.get(i).getTitle());
                    movie.setVote_average(movieList.get(i).getVote_average());
                    movie.setOverview(movieList.get(i).getOverview());
                    movie.setBackdrop_path(movieList.get(i).getBackdrop_path());
                    movie.setPoster_path(movieList.get(i).getPoster_path());

                    offLineMovies.add(movie);
                }
                return offLineMovies;
            }

            @Override
            protected void onPostExecute(ArrayList<MovieModel.Result> movie) {
                movieView.onOffLineDataSuccess(movie);
                //AppLog.showLog("OFFLINE DATA : ", movie.size() + "");
                super.onPostExecute(movie);
            }
        }

        GetAllMovies get_all_movies = new GetAllMovies();
        get_all_movies.execute();


    }

    @Override
    public void getMovieFromDataBase(String movieId) {
        class GetMovie extends AsyncTask<Void, Void, Movie> {

            @Override
            protected Movie doInBackground(Void... voids) {

                //get the movie by id to load all information
                Movie movie = DatabaseClient
                        .getInstance(mContext)
                        .getAppDatabase()
                        .movieDAO()
                        .getMovieById(movieId);


                return movie;
            }

            @Override
            protected void onPostExecute(Movie movie) {
                movieDetailView.onOffLineDataSuccess(movie);
                super.onPostExecute(movie);
            }
        }

        GetMovie get_movie = new GetMovie();
        get_movie.execute();
    }

    @Override
    public void InsertMoviesToDataBase(ArrayList<MovieModel.Result> movieList) {
//        Toast.makeText(mContext, product.getProductName(), Toast.LENGTH_SHORT).show();
        class SaveMovie extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //delete all movies before inserting new data to database
                DatabaseClient.getInstance(mContext).getAppDatabase()
                        .movieDAO()
                        .deleteAll();

                for (int i = 0; i < movieList.size(); i++) {
                    //creating a movie
                    Movie movie = new Movie();
                    movie.setMovieId(String.valueOf(movieList.get(i).getId()));
                    movie.setTitle(movieList.get(i).getTitle());
                    movie.setVote_average(movieList.get(i).getVote_average());
                    movie.setOverview(movieList.get(i).getOverview());
                    movie.setBackdrop_path(movieList.get(i).getBackdrop_path());
                    movie.setPoster_path(movieList.get(i).getPoster_path());


                    //adding movie to database
                    DatabaseClient.getInstance(mContext).getAppDatabase()
                            .movieDAO()
                            .insert(movie);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                movieView.onFail("Inserted Successfully");
                super.onPostExecute(aVoid);
            }
        }

        SaveMovie save_movie = new SaveMovie();
        save_movie.execute();
    }

    @Override
    public void UpdateMovieToDataBase(String movieId, String genre, String releasedDate,
                                      String duration, String collection, String prodCompany, String prodCountry,
                                      String posterPath,String backDropPath,String homePageLink) {

        class UpdateMovie extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //get the movie by id to update the information
                Movie movie = DatabaseClient
                        .getInstance(mContext)
                        .getAppDatabase()
                        .movieDAO()
                        .getMovieById(movieId);

                movie.setGenres(genre);
                movie.setRelease_date(releasedDate);
                movie.setDuration(duration);
                movie.setBelongs_to_collection(collection);
                movie.setProduction_companies(prodCompany);
                movie.setProduction_countries(prodCountry);
                movie.setCollection_poster_path(posterPath);
                movie.setCollection_dropback_path(backDropPath);
                movie.setHomepage(homePageLink);
                movie.setStatus(true);


                //insert the movie after update
                DatabaseClient
                        .getInstance(mContext)
                        .getAppDatabase()
                        .movieDAO()
                        .update(movie);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                movieDetailView.onFail("Updated Successfully");
                super.onPostExecute(aVoid);
            }
        }

        UpdateMovie update_movie = new UpdateMovie();
        update_movie.execute();
    }


    /* method to call an api and return the movie detail*/
    void FetchMovieDetailFromAPI(String movieId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getMovieDetail(movieId).enqueue(new Callback<MovieDetailModel>() {
            @Override
            public void onResponse(Call<MovieDetailModel> call, Response<MovieDetailModel> response) {

                MovieModel movie = null;
                String s = null;


                if (response.isSuccessful()) {


                    AppLog.showLog("RESPONSE ", response.body().toString());

                    //JsonObject post = new JsonObject().get(response.body().toString()).getAsJsonObject();
                    //  AppLog.showLog("RESPONSE ", post.toString());
//                    AppLog.showLog("RESPONSE ", String.valueOf(response.body().getResultList().size()));

                    //returm movies details
                    movieDetailView.onSucces(response.body());

                } else if (response.code() == 401) {
                    movieView.onFail(ErrorUtils.parseError(response).getMsg());
                }
            }

            @Override
            public void onFailure(Call<MovieDetailModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("TAG", "error on fetching movie : " + t.getMessage());
            }
        });
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

//                    AppLog.showLog("RESPONSE ", response.toString());
//                    AppLog.showLog("RESPONSE ", String.valueOf(response.body().getPage()));
//                    AppLog.showLog("RESPONSE ", String.valueOf(response.body().getResultList().size()));

                    //save movie to datadase
                    InsertMoviesToDataBase(response.body().getResultList());

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
