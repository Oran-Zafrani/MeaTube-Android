package com.example.footube;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class MoviesList extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_MOVIE = 1;

    private DrawerLayout drawerLayout;
    private ImageButton sideMenuButton;
    private ImageButton signInButton;
    private ImageView userImage;
    private TextView userName;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new MovieAdapter(MoviesManager.getInstance().getMovies());
        recyclerView.setAdapter(adapter);

        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        // Initialize the views
        signInButton = findViewById(R.id.signin);
        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);

        // Set visibility based on whether the user is present
        if (user != null) {
            signInButton.setVisibility(View.GONE);
            userImage.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);

            userName.setText(user.getDisplayName());
            setImage(user.getImage());
        } else {
            signInButton.setVisibility(View.VISIBLE);
            userImage.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInIntent = new Intent(MoviesList.this, SignIn.class);
                    startActivity(signInIntent);
                }
            });
        }

        FloatingActionButton btnAddMovie = findViewById(R.id.btnAddMovie);
        btnAddMovie.setOnClickListener(view -> {
            try {
                // Create an Intent to start the new activity
                if (user != null) {
                    Intent addMovieIntent = new Intent(this, AddMovie.class);
                    addMovieIntent.putExtra("user", user);
                    startActivityForResult(addMovieIntent, REQUEST_CODE_ADD_MOVIE);
                } else {
                    Intent signInIntent = new Intent(MoviesList.this, SignIn.class);
                    startActivity(signInIntent);
                }
            } catch (Exception e) {
                Intent signInIntent = new Intent(MoviesList.this, SignIn.class);
                startActivity(signInIntent);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuButton = findViewById(R.id.side_menu_button);

        // Set OnClickListener for side menu button
        sideMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when button is clicked
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the RecyclerView with the latest movies
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_MOVIE && resultCode == RESULT_OK) {
            // Update the RecyclerView with the latest movies
            adapter.notifyDataSetChanged();
        }
    }

    public static Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void setImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (!imagePath.equals("default_profile.jpg")) {
                Bitmap myBitmap = base64ToBitmap(imagePath);
                // Set the decoded Bitmap to the ImageView
                userImage.setImageBitmap(myBitmap);
            } else {
                // Handle image not found
                userImage.setImageResource(R.drawable.signin_man); // Default image resource
            }
        } else {
            // Handle case where imagePath is null or empty
            userImage.setImageResource(R.drawable.signin_man); // Default image resource
        }
    }
}
