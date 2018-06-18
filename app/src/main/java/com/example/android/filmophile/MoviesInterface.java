package com.example.android.filmophile;

import android.widget.Adapter;

import com.example.android.filmophile.Adapters.MovieAdapter;

public interface MoviesInterface {
    void onMovieSelected(MovieAdapter adapter);
}
