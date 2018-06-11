package com.example.android.filmophile.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.filmophile.R;
import com.example.android.filmophile.Utility.Utils;

public class HomeActivity extends AppCompatActivity {

    //TODO: Enter your api key in api_key tag in the res/strings

    public Context context;
    public RecyclerView recyclerView;
    public Toolbar homeToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setExitTransition(new Explode());
        getWindow().setEnterTransition(new Explode());
        context = this;
        Utils.setBaseUrls(getString(R.string.api_key));
        homeToolBar = findViewById(R.id.home_tool_bar);
        setSupportActionBar(homeToolBar);
        recyclerView = findViewById(R.id.movie_list);
        Utils.dataRequestForPopular(context, recyclerView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        item.setChecked(true);
        switch (itemSelected) {

            case R.id.menuSortPopular:
                Utils.dataRequestForPopular(context, recyclerView);
                break;

            case R.id.menuSortRating:
                Utils.dataRequestForRating(context, recyclerView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
