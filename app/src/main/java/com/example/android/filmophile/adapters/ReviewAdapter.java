package com.example.android.filmophile.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.filmophile.model.ReviewResult;
import com.example.android.filmophile.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private final List<ReviewResult> reviewResultList;

    public ReviewAdapter(List<ReviewResult> reviewList) {
        this.reviewResultList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewResult data = reviewResultList.get(position);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(data.getAuthor().substring(0, 1),
                        generator.getRandomColor());

        holder.avatarImageView.setImageDrawable(drawable);
        holder.authorTextView.setText(data.getAuthor());
        holder.reviewTextView.setText(data.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewResultList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        final ImageView avatarImageView;
        final TextView authorTextView;
        final TextView reviewTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatar_image_view);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
        }
    }

}
