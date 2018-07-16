package com.example.android.filmophile.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouriteMovieDao {

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<GeneralMovieInfo>> getAllMovies();

    @Query("SELECT * FROM favourite_movies WHERE id = :id")
    List<GeneralMovieInfo> getMovieById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(GeneralMovieInfo favouriteMovie);

    @Query("DELETE FROM favourite_movies WHERE id = :id")
    void deleteMovie(Integer id);

}
