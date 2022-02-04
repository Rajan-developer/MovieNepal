package com.rajan.movienepal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.rajan.movienepal.adapter.ImageRecyclerViewAdapter;
import com.rajan.movienepal.controller.movie.MovieController;
import com.rajan.movienepal.database.entity.Movie;
import com.rajan.movienepal.model.movie.MovieDetailModel;
import com.rajan.movienepal.utility.AppText;
import com.rajan.movienepal.utility.AppUtil;
import com.rajan.movienepal.view.IMovieDetailView;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements IMovieDetailView, View.OnClickListener {

    //variables
    final public static String MOVIE_ID = "movie_id";
    Intent intent;
    String movieId;

    //variable for exoplayer
    SimpleExoPlayer simpleExoPlayer;
    boolean flag = false;

    //view
    ImageView backPress;
    FrameLayout playerFrameLayout, movieFrameLayout;
    TextView cannotPlayVideo;
    RelativeLayout rlCannotPlay;
    ImageView imageCannotPlay;
    ImageView movieImage;
    TextView movieName, movieGeneres, movieReleaseDate, movieTimeInterval, movieRating, movieOverView, movieCollection, movieProductionCompany, movieProductionCountry;
    RecyclerView movieImageRecyclerView;
    RelativeLayout progressBar;
    RelativeLayout noInternet;

    /*views for exo player*/
    PlayerView playerView;
    ProgressBar playerProgressBar;
    ImageView btnFullScreen;


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

        backPress.setOnClickListener(this);


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Flag " + flag, Toast.LENGTH_SHORT).show();
            flag = true;
            btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
        }


        //controller
        movieController = new MovieController(this, MovieDetailActivity.this);


        //adapter
        movieImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        if (AppUtil.isInternetConnectionAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            movieController.getMovieDetail(movieId);
        } else {
            //show cannot play UI when offline
            rlCannotPlay.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            movieController.getMovieFromDataBase(movieId);
        }


    }

    public void intiView() {
        backPress = findViewById(R.id.back_pressed);
        playerFrameLayout = findViewById(R.id.playerFrameLayout);
        movieFrameLayout = findViewById(R.id.movieFrameLayout);
        rlCannotPlay = findViewById(R.id.rl_cannot_play);
        imageCannotPlay = findViewById(R.id.iv_cannot_play);
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

        //initializing views for exo player
        cannotPlayVideo = findViewById(R.id.tv_cannotPlay);
        rlCannotPlay.setVisibility(View.GONE);
        playerView = findViewById(R.id.player_view);
        playerProgressBar = findViewById(R.id.progressBar);
        btnFullScreen = findViewById(R.id.btn_fullscreen);
        btnFullScreen.setVisibility(View.GONE);

        //video url
        Uri videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4");
        //Uri videoUrl = Uri.parse("https://www.youtube.com/watch?v=JfVOs4VSpmA");
        // Uri videoUrl = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3");
        //Uri videoUrl = Uri.parse(" https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");

        //initialize load control
        LoadControl loadControl = new DefaultLoadControl();

        //Initialize band width meter
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        //Initialize track selector
        TrackSelector trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );

        //initialize simple exo player
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(MovieDetailActivity.this, trackSelector, loadControl);

        //initialize data source factory
        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

        //initialize extractors factory
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        //initialize media source
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl, factory, extractorsFactory, null, null);

        //set player
        playerView.setPlayer(simpleExoPlayer);

        //keep secreen on
        playerView.setKeepScreenOn(true);

        //prepare media
        simpleExoPlayer.prepare(mediaSource);

        //play video when ready
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //check condition
                if (playbackState == Player.STATE_READY) {
                    //when ready
                    //Hide progree bar
                    playerProgressBar.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_ENDED) {
                    //when ready
                    //Hide progree bar
                    playerProgressBar.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_IDLE) {
                    //when ready
                    //Hide progree bar
                    playerProgressBar.setVisibility(View.GONE);
                } else {
                    //when buffering
                    //show progress bar
                    playerProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });


        btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieDetailActivity.this, "Clicked" + flag, Toast.LENGTH_SHORT).show();


               /* int orientation = getActivity().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }*/

                //check condition
                if (flag) {
                    //when flag is true
                    //set enter full screen image
                    btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    //set portrait orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    //set flag value to false
                    flag = false;
                } else {
                    //when flag is false
                    //set exit full screen image
                    btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    //set landscape orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //set flag value to true
                    flag = true;
                }
            }
        });
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
        String belongsPosterPath = "";
        String belongsBackDropPath = "";
        try {
            belongsToCollection = movieDetail.getBelongs_to_collection().getName();
            movieCollection.setText(belongsToCollection);

            belongsPosterPath = movieDetail.getBelongs_to_collection().getPoster_path();
            belongsBackDropPath = movieDetail.getBelongs_to_collection().getBackdrop_path();

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
                belongsPosterPath,
                belongsBackDropPath,
                movieDetail.getHomepage()
        );

    }

    @Override
    public void onOffLineDataSuccess(Movie movie) {

        //load image instead of player when offline
        Glide.with(this)
                .load(AppText.IMAGE_URL + movie.getBackdrop_path())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_not_found)
                .into(imageCannotPlay);

        //creating the list of images if not empty
        ArrayList<String> imagePaths = new ArrayList<>();
        if (!movie.getBackdrop_path().isEmpty()) imagePaths.add(movie.getBackdrop_path());
        if (!movie.getPoster_path().isEmpty()) imagePaths.add(movie.getPoster_path());

        //handling null pointer if the image not found
        try {
            if (!movie.getCollection_poster_path().isEmpty())
                imagePaths.add(movie.getCollection_poster_path());
            if (!movie.getCollection_dropback_path().isEmpty())
                imagePaths.add(movie.getCollection_dropback_path());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //related images of the movie
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(this);
        imageRecyclerViewAdapter.addImages(imagePaths);
        movieImageRecyclerView.setAdapter(imageRecyclerViewAdapter);


        //if the detail of movie is not saved in database then just display no internet msg
        if (movie.getStatus()) {
            AppUtil.showSnackBar(movieImageRecyclerView, this, getString(R.string.off_line_mode));
            noInternet.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }

        //set values to UI
        movieName.setText(movie.getTitle());
        Glide.with(this)
                .load(AppText.IMAGE_URL + movie.getPoster_path())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_not_found)
                .into(movieImage);


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


    @Override
    protected void onPause() {
        super.onPause();
        //stop video when ready
        simpleExoPlayer.setPlayWhenReady(false);
        //get playback state
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //play video when ready
        simpleExoPlayer.setPlayWhenReady(true);
        //Get playback state
        simpleExoPlayer.getPlaybackState();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_pressed:
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_left);
    }
}
