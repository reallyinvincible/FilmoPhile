package com.example.android.filmophile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailers {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private final List<TrailerResult> results = null;

    public List<TrailerResult> getResults() {
        return results;
    }

}
