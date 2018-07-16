package com.example.android.filmophile.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.android.filmophile.Model.TrailerResult;
import com.example.android.filmophile.R;
import com.gtomato.android.ui.widget.CarouselView;

import java.util.List;

public class TrailerAdapter extends CarouselView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private final List<TrailerResult> trailerResultList;

    public TrailerAdapter(List<TrailerResult> trailerList) {
        this.trailerResultList = trailerList;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {
        final TrailerResult data = trailerResultList.get(position);
        Glide.with(holder.trailerImage.getContext())
                .load("http://img.youtube.com/vi/"+data.getKey()+"/0.jpg")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.trailerImage);
        holder.trailerTextView.setText(data.getName());
        holder.trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(holder.trailerImage.getContext(), data.getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerResultList.size();
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{

        final ImageView trailerImage;
        TextView trailerTextView;

        TrailerViewHolder(View itemView) {
            super(itemView);
            trailerImage = itemView.findViewById(R.id.trailerThumbnail);
            trailerTextView = itemView.findViewById(R.id.trailerTextView);
        }
    }

}
