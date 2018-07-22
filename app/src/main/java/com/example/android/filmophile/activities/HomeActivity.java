package com.example.android.filmophile.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.filmophile.adapters.MovieAdapter;
import com.example.android.filmophile.database.GeneralMovieInfo;
import com.example.android.filmophile.database.MovieViewModel;
import com.example.android.filmophile.utility.MoviesInterface;
import com.example.android.filmophile.R;
import com.example.android.filmophile.utility.Utils;

import java.util.List;
import java.util.Objects;


public class HomeActivity extends AppCompatActivity {


    //TODO: Enter your api key in api_key tag in the res/strings

    private final int POPULAR_SELECTED = 0;
    private final int RATED_SELECTED = 1;
    private final int FAVOURITE_SELECTED = 2;

    private int movieSelected;

    private SharedPreferences sharedPreferences;
    private Context context;
    private RecyclerView recyclerView;
    private MoviesInterface moviesInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setExitTransition(new Explode());
        getWindow().setEnterTransition(new Explode());
        context = this;
        Utils.setBaseUrls(getString(R.string.api_key));
        Toolbar homeToolBar = findViewById(R.id.home_tool_bar);
        setSupportActionBar(homeToolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        recyclerView = findViewById(R.id.movie_list);
        recyclerView.setHasFixedSize(true);
        moviesInterface = new MoviesInterface() {
            @Override
            public void onMovieSelected(MovieAdapter adapter) {
                recyclerView.setAdapter(adapter);
            }
        };

        sharedPreferences = getSharedPreferences("MovieInfo", Context.MODE_PRIVATE);

        switch (sharedPreferences.getInt("MovieSelected", 3)) {
            case POPULAR_SELECTED:
                setPopular();
                break;
            case RATED_SELECTED:
                setRated();
                break;
            case FAVOURITE_SELECTED:
                setFavourite();
                break;
            default:
                setPopular();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_options, menu);

        switch (sharedPreferences.getInt("MovieSelected", 3)) {
            case POPULAR_SELECTED:
                menu.findItem(R.id.menuSortPopular).setChecked(true);
                break;
            case RATED_SELECTED:
                menu.findItem(R.id.menuSortRating).setChecked(true);
                break;
            case FAVOURITE_SELECTED:
                menu.findItem(R.id.menuFavourite).setChecked(true);
                break;
            default:
                menu.findItem(R.id.menuSortPopular).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        item.setChecked(true);
        switch (itemSelected) {

            case R.id.menuSortPopular:
                setPopular();
                break;

            case R.id.menuSortRating:
                setRated();
                break;

            case R.id.menuFavourite:
                setFavourite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavourite() {
        movieSelected = FAVOURITE_SELECTED;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("MovieSelected", movieSelected).commit();
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovieList().observe(this, new Observer<List<GeneralMovieInfo>>() {
            @Override
            public void onChanged(@Nullable List<GeneralMovieInfo> generalMovieInfos) {
                if (movieSelected == FAVOURITE_SELECTED) {
                    findViewById(R.id.noFavouritesTextView).setVisibility(View.INVISIBLE);
                    MovieAdapter adapter = new MovieAdapter(getApplicationContext(), null, generalMovieInfos);
                    recyclerView.setAdapter(adapter);
                    if (generalMovieInfos.size() == 0) {
                        findViewById(R.id.retryButton).setVisibility(View.GONE);
                        findViewById(R.id.noFavouritesTextView).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void setPopular() {
        movieSelected = POPULAR_SELECTED;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("MovieSelected", movieSelected).commit();
        findViewById(R.id.noFavouritesTextView).setVisibility(View.INVISIBLE);
        Utils.dataRequestForPopular(context, moviesInterface);
    }

    public void setRated() {
        movieSelected = RATED_SELECTED;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("MovieSelected", movieSelected).commit();
        findViewById(R.id.noFavouritesTextView).setVisibility(View.INVISIBLE);
        Utils.dataRequestForRating(context, moviesInterface);
    }

}
