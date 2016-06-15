package com.android.dev.sineth.ytsmoviefactory.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.dev.sineth.ytsmoviefactory.Database.DBController;
import com.android.dev.sineth.ytsmoviefactory.Models.Movie;
import com.android.dev.sineth.ytsmoviefactory.R;
import com.android.dev.sineth.ytsmoviefactory.RecycleViewAdapter.RecycleViewAdapterMovie;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecycleViewAdapterMovie adapterMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapterMovie = new RecycleViewAdapterMovie(this);
        mRecyclerView.setAdapter(adapterMovie);
        ArrayList<Movie> movieSaved = DBController.getMovieAll(this);
        adapterMovie.setMovieList(movieSaved);
        adapterMovie.notifyDataSetChanged();

    }

}
