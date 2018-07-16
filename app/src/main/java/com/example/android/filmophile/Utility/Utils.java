package com.example.android.filmophile.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.filmophile.Adapters.MovieAdapter;
import com.example.android.filmophile.Model.Result;
import com.example.android.filmophile.Model.Movies;
import com.example.android.filmophile.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.List;

public class Utils {

    private static String POPULAR_BASE_URL;
    private static String TOP_RATED_BASE_URL;
    public static String MOVIE_TRAILER_URL;
    public static String MOVIE_REVIEW_URL;

    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w400/";
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w1280/";
    private static final int POPULAR_CALLING_ID = 1;
    private static final int RATING_CALLING_ID = 2;

    private static List<Result> allMoviesByPopularity;
    private static List<Result> allMoviesByRating;

    public static void setBaseUrls(String apiKey) {
        POPULAR_BASE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;
        TOP_RATED_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + apiKey;
        MOVIE_TRAILER_URL = "/videos?api_key=" + apiKey;
        MOVIE_REVIEW_URL = "/reviews?api_key=" + apiKey;

    }

    private static List<Result> getAllMoviesByPopularity() {
        return allMoviesByPopularity;
    }

    private static void setAllMoviesByPopularity(List<Result> allMoviesByPopularity) {
        Utils.allMoviesByPopularity = allMoviesByPopularity;
    }

    private static List<Result> getAllMoviesByRating() {
        return allMoviesByRating;
    }

    private static void setAllMoviesByRating(List<Result> allMoviesByRating) {
        Utils.allMoviesByRating = allMoviesByRating;
    }

    public static void dataRequestForPopular(final Context context, final MoviesInterface moviesInterface) {

        Activity activity = (Activity) context;
        final RelativeLayout relativeLayout = activity.findViewById(R.id.signup_loading_screen);
        relativeLayout.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(POPULAR_BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                relativeLayout.setVisibility(View.GONE);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Movies movies = gson.fromJson(response, Movies.class);
                setAllMoviesByPopularity(movies.getResults());
                MovieAdapter adapter = new MovieAdapter(context, getAllMoviesByPopularity(), null);
                moviesInterface.onMovieSelected(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                relativeLayout.setVisibility(View.GONE);
                isConnectedToInternet(context, POPULAR_CALLING_ID, moviesInterface);
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void dataRequestForRating(final Context context, final MoviesInterface moviesInterface) {

        Activity activity = (Activity) context;
        final RelativeLayout relativeLayout = activity.findViewById(R.id.signup_loading_screen);
        relativeLayout.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(TOP_RATED_BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                relativeLayout.setVisibility(View.GONE);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Movies movies = gson.fromJson(response, Movies.class);
                setAllMoviesByRating(movies.getResults());
                MovieAdapter adapter = new MovieAdapter(context, getAllMoviesByRating(), null);
                moviesInterface.onMovieSelected(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                relativeLayout.setVisibility(View.GONE);
                isConnectedToInternet(context, RATING_CALLING_ID, moviesInterface);
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private static void isConnectedToInternet(final Context context, final int callingId, final MoviesInterface moviesInterface) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            new FancyAlertDialog.Builder((Activity)context)
                    .setTitle("Error")
                    .setBackgroundColor(Color.parseColor("#CCD50000"))
                    .setMessage("Something went wrong! Hit Retry")
                    .setNegativeBtnText("Quit")
                    .setPositiveBtnBackground(Color.parseColor("#CCD50000"))
                    .setPositiveBtnText("Retry")
                    .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                    .setAnimation(Animation.POP)
                    .isCancellable(false)
                    .setIcon(R.drawable.ic_error_outline, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            switch (callingId){
                                case POPULAR_CALLING_ID:
                                    dataRequestForPopular(context, moviesInterface);
                                    break;

                                case RATING_CALLING_ID:
                                    dataRequestForRating(context, moviesInterface);
                                    break;
                            }
                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            ((Activity) context).finish();
                        }
                    })
                    .build();
        } else {
            new FancyAlertDialog.Builder((Activity)context)
                    .setTitle("No Internet Connection")
                    .setBackgroundColor(Color.parseColor("#CCD50000"))
                    .setMessage("Please turn on your connection and hit Retry")
                    .setNegativeBtnText("Quit")
                    .setPositiveBtnBackground(Color.parseColor("#CCD50000"))
                    .setPositiveBtnText("Retry")
                    .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                    .setAnimation(Animation.POP)
                    .isCancellable(false)
                    .setIcon(R.drawable.ic_error_outline, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            switch (callingId){
                                case POPULAR_CALLING_ID:
                                    dataRequestForPopular(context, moviesInterface);
                                    break;

                                case RATING_CALLING_ID:
                                    dataRequestForRating(context, moviesInterface);
                                    break;
                            }
                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            ((Activity) context).finish();
                        }
                    })
                    .build();
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

