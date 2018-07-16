package com.example.android.filmophile.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.filmophile.Adapters.MovieAdapter;
import com.example.android.filmophile.Database.FavouriteMoviesDatabase;
import com.example.android.filmophile.Database.GeneralMovieInfo;
import com.example.android.filmophile.Database.MovieViewModel;
import com.example.android.filmophile.Utility.MoviesInterface;
import com.example.android.filmophile.R;
import com.example.android.filmophile.Utility.Utils;

import java.util.List;
import java.util.Objects;


public class HomeActivity extends AppCompatActivity {


    //TODO: Enter your api key in api_key tag in the res/strings

    private Context context;
    private RecyclerView recyclerView;
    private MoviesInterface moviesInterface;
    private boolean favSelected = false;

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
        Utils.dataRequestForPopular(context, moviesInterface);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        item.setChecked(true);
        switch (itemSelected) {

            case R.id.menuSortPopular:
                favSelected = false;
                findViewById(R.id.noFavouritesTextView).setVisibility(View.INVISIBLE);
                Utils.dataRequestForPopular(context, moviesInterface);
                break;

            case R.id.menuSortRating:
                favSelected = false;
                findViewById(R.id.noFavouritesTextView).setVisibility(View.INVISIBLE);
                Utils.dataRequestForRating(context, moviesInterface);
                break;

            case R.id.menuFavourite:
                favSelected = true;
                dataRequestForFavourite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dataRequestForFavourite() {
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovieList().observe(this, new Observer<List<GeneralMovieInfo>>() {
            @Override
            public void onChanged(@Nullable List<GeneralMovieInfo> generalMovieInfos) {
                if (favSelected) {
                    findViewById(R.id.noFavouritesTextView).setVisibility(View.INVISIBLE);
                    MovieAdapter adapter = new MovieAdapter(getApplicationContext(), null, generalMovieInfos);
                    recyclerView.setAdapter(adapter);
                    if(generalMovieInfos.size() == 0){
                        findViewById(R.id.noFavouritesTextView).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}
