package com.example.android.filmophile.Utility;

import android.content.Context;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.filmophile.Adapters.MovieAdapter;
import com.example.android.filmophile.Model.Movies;
import com.example.android.filmophile.Model.Result;
import com.example.android.filmophile.MoviesInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class Utils {

    private static String POPULAR_BASE_URL;
    private static String TOP_RATED_BASE_URL;
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w400/";
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w1280/";

    private static List<Result> allMoviesByPopularity;
    private static List<Result> allMoviesByRating;

    public static void setBaseUrls(String apiKey) {
        POPULAR_BASE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;
        TOP_RATED_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + apiKey;
    }

    public static List<Result> getAllMoviesByPopularity() {
        return allMoviesByPopularity;
    }

    private static void setAllMoviesByPopularity(List<Result> allMoviesByPopularity) {
        Utils.allMoviesByPopularity = allMoviesByPopularity;
    }

    public static List<Result> getAllMoviesByRating() {
        return allMoviesByRating;
    }

    private static void setAllMoviesByRating(List<Result> allMoviesByRating) {
        Utils.allMoviesByRating = allMoviesByRating;
    }

    public static void dataRequestForPopular(final Context context, final MoviesInterface moviesInterface) {

        StringRequest request = new StringRequest(POPULAR_BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Movies movies = gson.fromJson(response, Movies.class);
                setAllMoviesByPopularity(movies.getResults());
                MovieAdapter adapter = new MovieAdapter(context, getAllMoviesByPopularity());
                moviesInterface.onMovieSelected(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NETWORK REQUEST", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void dataRequestForRating(final Context context, final MoviesInterface moviesInterface) {
        StringRequest request = new StringRequest(TOP_RATED_BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Movies movies = gson.fromJson(response, Movies.class);
                setAllMoviesByRating(movies.getResults());
                MovieAdapter adapter = new MovieAdapter(context, getAllMoviesByRating());
                moviesInterface.onMovieSelected(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NETWORK REQUEST", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Toast.makeText(context, "You are now connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public static String formatDate(String currentDate){
        String date = currentDate.substring(8, 10);
        String monthNumeral = currentDate.substring(5,7);
        String year = currentDate.substring(0,4);
        String month = null;
        switch (Integer.parseInt(monthNumeral)){
            case 1: month = "January"; break;
            case 2: month = "February"; break;
            case 3: month = "March"; break;
            case 4: month = "April"; break;
            case 5: month = "May"; break;
            case 6: month = "June"; break;
            case 7: month = "July"; break;
            case 8: month = "August"; break;
            case 9: month = "September"; break;
            case 10: month = "October"; break;
            case 11: month = "November"; break;
            case 12: month = "December"; break;
        }
        return date + " " + month + ", " +year;
    }

}

