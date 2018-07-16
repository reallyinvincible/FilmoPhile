package com.example.android.filmophile.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movies {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    /**
     * No args constructor for use in serialization
     */
    public Movies() {
    }

    /**
     * @param results
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public Movies(Integer page, Integer totalResults, Integer totalPages, List<Result> results) {
        super();
        Integer page1 = page;
        Integer totalResults1 = totalResults;
        Integer totalPages1 = totalPages;
        this.results = results;
    }

    public List<Result> getResults() {
        return results;
    }

}