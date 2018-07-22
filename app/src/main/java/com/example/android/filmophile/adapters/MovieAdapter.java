package com.example.android.filmophile.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.filmophile.activities.DetailActivity;
import com.example.android.filmophile.database.GeneralMovieInfo;
import com.example.android.filmophile.model.Result;
import com.example.android.filmophile.R;
import com.example.android.filmophile.utility.Utils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private final List<Result> movieData;
    private final List<GeneralMovieInfo> favData;
    private int lastPosition = -1;

    public MovieAdapter(Context parentContext, List<Result> allMovies, List<GeneralMovieInfo> favouriteMovies) {
        this.context = parentContext;
        this.movieData = allMovies;
        this.favData = favouriteMovies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        GeneralMovieInfo generalMovie;
        if(favData == null) {
            Result data = movieData.get(position);
            generalMovie = new GeneralMovieInfo(data.getId(), data.getVoteCount(),
                    data.getVoteAverage(), data.getTitle(), data.getPopularity(), data.getPosterPath(),
                    data.getOriginalTitle(), data.getBackdropPath(), data.getOverview(), data.getReleaseDate());
        }
        else{
            generalMovie = favData.get(position);
        }
        showMovies(holder, generalMovie);
    }

    private void showMovies(final MovieViewHolder holder, final GeneralMovieInfo movie){
        Glide.with(holder.poster.getContext())
                .load(Utils.IMAGE_BASE_URL + movie.getPosterPath())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().placeholder(R.drawable.loading_placeholder))
                .into(holder.poster);

        holder.title.setText(movie.getTitle());

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ResultParcel", movie);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.poster.getContext(),
                        holder.poster, context.getString(R.string.transition_name))
                        .toBundle();
                holder.poster.getContext().startActivity(intent, bundle);
            }
        });

        setAnimation(holder.itemView, holder.getAdapterPosition());
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if (favData == null){
            return movieData.size();
        }
        else {
            return favData.size();
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        final ImageView poster;
        final TextView title;

        private MovieViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_image_view);
            title = itemView.findViewById(R.id.poster_title_text_view);
        }
    }
}
