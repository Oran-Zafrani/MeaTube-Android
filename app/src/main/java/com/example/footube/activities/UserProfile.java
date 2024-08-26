package com.example.footube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footube.BasicClasses.User;
import com.example.footube.R;
import com.example.footube.ViewModel.UserViewModel;
import com.example.footube.listeners.MovieAdapter;
import com.example.footube.managers.MoviesManager;

import java.util.Objects;

public class UserProfile extends AppCompatActivity implements MovieAdapter.OnMovieClickListener{

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private Button deleteuser;
    private UserViewModel userviewModel;
    private User user;
    private String loggedInUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        loggedInUserName = getIntent().getStringExtra("username");

        //define user view model
        userviewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new MovieAdapter(MoviesManager.getInstance(this).getMovies(),this);
        recyclerView.setAdapter(adapter);

        //define delete button
        deleteuser = findViewById(R.id.btn_delete_user);
        deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                new AlertDialog.Builder(v.getContext()) // or YourActivityName.this
                        .setMessage("Are you sure you want to delete the user?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Perform the delete operation
                             userviewModel.deleteUser(loggedInUserName);
                             //finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


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