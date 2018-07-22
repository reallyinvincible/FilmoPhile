package com.example.android.filmophile.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.android.filmophile.adapters.ReviewAdapter;
import com.example.android.filmophile.adapters.TrailerAdapter;
import com.example.android.filmophile.database.FavouriteMoviesDatabase;
import com.example.android.filmophile.database.GeneralMovieInfo;
import com.example.android.filmophile.model.ReviewResult;
import com.example.android.filmophile.model.Reviews;
import com.example.android.filmophile.model.TrailerResult;
import com.example.android.filmophile.model.Trailers;
import com.example.android.filmophile.R;
import com.example.android.filmophile.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gtomato.android.ui.transformer.FlatMerryGoRoundTransformer;
import com.gtomato.android.ui.widget.CarouselView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private FavouriteMoviesDatabase moviesDatabase;
    private GeneralMovieInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        data = intent.getParcelableExtra("ResultParcel");
        ImageView posterDetailView = findViewById(R.id.poster_image_detail);
        ImageView backdropDetailView = findViewById(R.id.movie_backdrop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView titleText = findViewById(R.id.titleTextView);
        TextView dateText = findViewById(R.id.dateTextView);
        TextView voteText = findViewById(R.id.voteTextView);
        TextView voteCountText = findViewById(R.id.voteCountTextView);
        TextView plotOverviewText = findViewById(R.id.plotSynopsisTextView);
        moviesDatabase = FavouriteMoviesDatabase.getInstance(getApplicationContext());
        ShineButton favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setShapeResource(R.raw.heart);
        favouriteButton.setBtnColor(Color.GRAY);
        favouriteButton.setBtnFillColor(Color.RED);
        favouriteButton.init(this);
        List<GeneralMovieInfo> moviesInfo = moviesDatabase.favouriteMovieDao().getMovieById(data.getId());
        if (moviesInfo.size() > 0) {
            favouriteButton.setChecked(true);
        }
        else {
            favouriteButton.setChecked(false);
        }
        favouriteButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                GeneralMovieInfo favouriteMovie = new GeneralMovieInfo(data.getId(), data.getVoteCount(),
                        data.getVoteAverage(), data.getTitle(), data.getPopularity(), data.getPosterPath(),
                        data.getOriginalTitle(), data.getBackdropPath(), data.getOverview(), data.getReleaseDate());
                if (checked) {
                    moviesDatabase.favouriteMovieDao().insertMovie(favouriteMovie);
                }
                else {
                    moviesDatabase.favouriteMovieDao().deleteMovie(data.getId());
                }
            }
        });
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(data.getOriginalTitle());
        }
        titleText.setText(data.getTitle());
        titleText.setElevation(16f);
        dateText.setText(Utils.formatDate(data.getReleaseDate()));
        voteText.setText(data.getVoteAverage().toString() + "/10");
        voteCountText.setText(data.getVoteCount().toString());
        plotOverviewText.setText(data.getOverview());

        findViewById(R.id.movie_backdrop).setBackground(getDrawable(R.drawable.default_backdrop));
        if (data.getPosterPath() != null)
            Glide.with(this)
                    .load(Utils.IMAGE_BASE_URL + data.getPosterPath())
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(posterDetailView);
        posterDetailView.setElevation(16f);
        Glide.with(this).load(Utils.BACKDROP_BASE_URL + data.getBackdropPath()).into(backdropDetailView);

        displayTrailers();
        displayReviews();
    }

    private void displayTrailers(){
        StringRequest request = new StringRequest(Utils.MOVIE_BASE_URL + data.getId().toString() + Utils.MOVIE_TRAILER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Trailers trailers = gson.fromJson(response, Trailers.class);
                        List<TrailerResult> trailerResultList = trailers.getResults();
                        TrailerAdapter adapter = new TrailerAdapter(trailerResultList);
                        CarouselView trailerCarouselView = findViewById(R.id.trailerCarousel);
                        trailerCarouselView.setScrollingAlignToViews(true);
                        trailerCarouselView.setInfinite(true);
                        trailerCarouselView.setTransformer(new FlatMerryGoRoundTransformer());
                        trailerCarouselView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void displayReviews(){

        StringRequest request = new StringRequest(Utils.MOVIE_BASE_URL + data.getId().toString() + Utils.MOVIE_REVIEW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Reviews reviews = gson.fromJson(response, Reviews.class);
                        List<ReviewResult> reviewResultList = reviews.getResults();
                        if (reviewResultList.size() == 0){
                            findViewById(R.id.reviewPlaceHolder).setVisibility(View.VISIBLE);
                            findViewById(R.id.reviewRecyclerView).setVisibility(View.GONE);
                        } else {
                            ReviewAdapter adapter = new ReviewAdapter(reviewResultList);
                            RecyclerView view = findViewById(R.id.reviewRecyclerView);
                            view.setVisibility(View.VISIBLE);
                            view.setAdapter(adapter);
                            view.setHasFixedSize(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

}
