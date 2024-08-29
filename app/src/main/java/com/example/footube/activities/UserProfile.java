package com.example.footube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.footube.localDB.LoggedInUser;
import com.example.footube.managers.MoviesManager;

import java.util.Objects;

public class UserProfile extends AppCompatActivity implements MovieAdapter.OnMovieClickListener{

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private Button deleteuser;
    private Button edituser;
    private UserViewModel userviewModel;
    private User user;
    private String loggedInUserName;
    private TextView userName;
    private ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        String movieCreator = getIntent().getStringExtra("movie_creator");

        // Define user view model
        userviewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Define delete button
        deleteuser = findViewById(R.id.btn_delete_user);

        // Define edit user button
        edituser = findViewById(R.id.btn_edit_user);

        //define textview of username and user image
        userName = findViewById(R.id.username);
        userImage = findViewById(R.id.uploader_image);

        if (LoggedInUser.thereIsUser()) {
            // Ensure LoggedInUser.getInstance().getUser() is not null
            User loggedInUser = LoggedInUser.getInstance().getUser();
            if (loggedInUser != null) {
                loggedInUserName = loggedInUser.getUsername();

                if (!Objects.equals(movieCreator, loggedInUserName)) {
                    deleteuser.setVisibility(View.GONE);
                    edituser.setVisibility(View.GONE);
                }
            } else {
                Log.e("UserProfile", "LoggedInUser.getUser() returned null.");
                // Handle the case where the user is null (e.g., finish the activity, show a message, etc.)
                deleteuser.setVisibility(View.GONE);
                edituser.setVisibility(View.GONE);
            }
        } else {
            Log.e("UserProfile", "No logged-in user found.");
            // Handle the case where there is no logged-in user
            deleteuser.setVisibility(View.GONE);
            edituser.setVisibility(View.GONE);
        }

        userviewModel.getUser(movieCreator);
        userviewModel.getUserLiveData().observe(this, userdata -> {
            userName.setText(userdata.getDisplayName());
            userImage.setImageBitmap(MoviesList.base64ToBitmap(userdata.getImage()));
        });


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new MovieAdapter(MoviesManager.getInstance(this).getMovies(), this);
        recyclerView.setAdapter(adapter);

        deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure you want to delete the user?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Perform the delete operation
                            if (loggedInUserName != null) {
                                userviewModel.deleteUser(loggedInUserName);
                            }
                            //finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editUserIntent = new Intent(UserProfile.this, EditUser.class);
                startActivity(editUserIntent);
            }
        });


    }

    public void onMovieClick(String position) {
        Intent movieDetailIntent = new Intent(this, VideoPlayerActivity.class);
        movieDetailIntent.putExtra("movie_index", position);
        if (user != null && !Objects.equals(user.getUsername(), "Guest")) {
            Log.d("movie123", user.getDisplayName());
            movieDetailIntent.putExtra("username", user.getUsername());
            movieDetailIntent.putExtra("Guest", 0);
        } else {
            Log.d("movie111", "user.getDisplayName()");
            movieDetailIntent.putExtra("Guest", 1);
        }
        startActivity(movieDetailIntent);
    }

}