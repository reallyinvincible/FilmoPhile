package com.example.android.filmophile.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {GeneralMovieInfo.class}, version = 2, exportSchema = false)
public abstract class FavouriteMoviesDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favourite_movies_db";
    private static FavouriteMoviesDatabase sInstance;

    public static FavouriteMoviesDatabase getInstance(Context context){
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouriteMoviesDatabase.class, FavouriteMoviesDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }
    public abstract FavouriteMovieDao favouriteMovieDao();
}
