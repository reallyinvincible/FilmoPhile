package com.example.android.filmophile.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.android.filmophile.Model.Result;
import com.example.android.filmophile.R;
import com.example.android.filmophile.Utility.Utils;

public class DetailActivity extends AppCompatActivity {

    public ImageView posterDetailView;
    public ImageView backdropDetailView;
    public Toolbar toolbar;
    public TextView titleText;
    public TextView dateText;
    public TextView voteText;
    public TextView voteCountText;
    public TextView plotOverviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        int position = intent.getIntExtra("SelectedPosition", -1);
        int selectedOrderType = intent.getIntExtra("SelectedOrderType", 0);
        posterDetailView = findViewById(R.id.poster_image_detail);
        backdropDetailView = findViewById(R.id.movie_backdrop);
        toolbar = findViewById(R.id.toolbar);
        titleText = findViewById(R.id.titleTextView);
        dateText = findViewById(R.id.dateTextView);
        voteText = findViewById(R.id.voteTextView);
        voteCountText = findViewById(R.id.voteCountTextView);
        plotOverviewText = findViewById(R.id.plotSynopsisTextView);
        setSupportActionBar(toolbar);

        Result data = null;
        switch (selectedOrderType) {
            case Utils.POPULAR_SELECTED:
                data = Utils.getAllMoviesByPopularity().get(position);
                break;
            case Utils.RATING_SELECTED:
                data = Utils.getAllMoviesByRating().get(position);
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(data.getOriginalTitle());
        }
        titleText.setText(data.getTitle());
        dateText.setText(data.getReleaseDate());
        voteText.setText(data.getVoteAverage().toString()+"/10");
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
