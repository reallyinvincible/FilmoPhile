package com.example.android.filmophile.Adapters;

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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.filmophile.Activities.DetailActivity;
import com.example.android.filmophile.Model.Result;
import com.example.android.filmophile.R;
import com.example.android.filmophile.Utility.Utils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Result> data;
    private int selectedOrder;


    public MovieAdapter(Context context, List<Result> allMovies, int selectType) {
        this.context = context;
        this.data = allMovies;
        this.selectedOrder = selectType;
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
        Result movie = data.get(position);
        Glide.with(holder.poster.getContext())
                .load(Utils.IMAGE_BASE_URL + movie.getPosterPath())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().placeholder(R.drawable.loading_placeholder))
                .into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("SelectedPosition", holder.getAdapterPosition());
                intent.putExtra("SelectedOrderType", selectedOrder);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                        holder.poster, context.getString(R.string.transition_name))
                        .toBundle();
                context.startActivity(intent, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;

        private MovieViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_image_view);
        }
    }
}
