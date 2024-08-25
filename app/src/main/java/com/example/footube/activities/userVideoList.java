package com.example.footube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footube.BasicClasses.User;
import com.example.footube.R;
import com.example.footube.listeners.MovieAdapter;
import com.example.footube.managers.MoviesManager;

import java.util.Objects;

public class userVideoList extends AppCompatActivity implements MovieAdapter.OnMovieClickListener{

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_video_list);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new MovieAdapter(MoviesManager.getInstance(this).getMovies(),this);
        recyclerView.setAdapter(adapter);

    }

    public void onMovieClick(int position) {
        Intent movieDetailIntent = new Intent(this, VideoPlayerActivity.class);
        movieDetailIntent.putExtra("movie_index", position);
        if(user != null && !Objects.equals(user.getUsername(), "Guest")){
            Log.d("movie123",user.getDisplayName());
            movieDetailIntent.putExtra("username", user.getUsername());
            movieDetailIntent.putExtra("Guest", 0);
        }else {
            Log.d("movie111","user.getDisplayName()");
            movieDetailIntent.putExtra("Guest", 1);
        }
        startActivity(movieDetailIntent);
    }
}