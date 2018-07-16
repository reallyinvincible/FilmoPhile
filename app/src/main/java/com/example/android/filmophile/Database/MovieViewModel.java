package com.example.android.filmophile.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final LiveData<List<GeneralMovieInfo>> movieList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        FavouriteMoviesDatabase moviesDatabase = FavouriteMoviesDatabase.getInstance(this.getApplication());
        movieList = moviesDatabase.favouriteMovieDao().getAllMovies();
    }

    public LiveData<List<GeneralMovieInfo>> getMovieList() {
        return movieList;
    }
}
