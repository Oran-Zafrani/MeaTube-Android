package com.example.footube;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class MoviesList extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private static final int REQUEST_CODE_ADD_MOVIE = 1;
    private static final String PREFS_NAME = "theme_prefs";
    private static final String PREF_DARK_MODE = "dark_mode";


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton sideMenuButton;
    private ImageButton signInButton;
    private ImageView userImage;
    private TextView userName;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private User user;
    private ImageButton SearchButton;
    private EditText SearchEditText;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new MovieAdapter(MoviesManager.getInstance(this).getMovies(),this);
        recyclerView.setAdapter(adapter);

        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

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

        SearchButton = findViewById(R.id.searchbutton);
        SearchEditText = findViewById(R.id.searchedittext);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Search1", "search");
//                Toast.makeText(MoviesList.this, "search: " + SearchEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                adapter.filter(SearchEditText.getText().toString());
            }
        });


//        View otherLayout = LayoutInflater.from(this).inflate(R.layout.navigation_menu, null);
//        TextView signout = otherLayout.findViewById(R.id.signout);
//
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

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
        navigationView = findViewById(R.id.navigation_view);
        sideMenuButton = findViewById(R.id.side_menu_button);

        // Set OnClickListener for side menu button
        sideMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when button is clicked
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Hide the "Sign Out" item if guest
        if (user == null){
            MenuItem signOutItem = navigationView.getMenu().findItem(R.id.signout);
            signOutItem.setVisible(false);
        }

        Intent Signin = new Intent(this, SignIn.class);
        // Set OnClickListener for the side menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.signout) {
                    startActivity(Signin);
                    finish();
                    return true;
                }
                if (id == R.id.darkmode) {
                    toggleTheme();
                    return true;
                }
                return false;
            }
        });

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the refresh operation
                refreshData();
            }
        });

//        SearchView searchView = findViewById(R.id.search_view);
//
////        searchView.setQuery("", true);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.filter(newText);
//                return false;
//            }
//        });

    }

    private void refreshData() {
        // Simulate a refresh operation (e.g., fetch new data)
        // After the operation is complete, call setRefreshing(false) to stop the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //cancel the filter (clean the search)
                SearchEditText.setText("");
                adapter.filter("");

                // Stop the refresh animation
                swipeRefreshLayout.setRefreshing(false);

//                adapter = new MovieAdapter(MoviesManager.getInstance().getMovies(),this);
//                recyclerView.setAdapter(adapter);

                // Update your data (e.g., notify your adapter of data changes)
                // yourAdapter.notifyDataSetChanged();
            }
        }, 2000); // Simulate a delay
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the RecyclerView with the latest movies
        adapter.notifyDataSetChanged();
        adapter.filter("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_MOVIE && resultCode == RESULT_OK) {
            // Update the RecyclerView with the latest movies
            adapter.notifyDataSetChanged();
        }
    }

    private void toggleTheme() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean(PREF_DARK_MODE, false);

        // Toggle theme
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            preferences.edit().putBoolean(PREF_DARK_MODE, false).apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            preferences.edit().putBoolean(PREF_DARK_MODE, true).apply();
        }

        // Recreate activity to apply the new theme
        recreate();
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


    public void onMovieClick(int position) {
        Intent movieDetailIntent = new Intent(this, VideoPlayerActivity.class);
        movieDetailIntent.putExtra("movie_index", position);
        if(user != null){
            movieDetailIntent.putExtra("username", user);
            movieDetailIntent.putExtra("Guest", 0);
        }else {
            movieDetailIntent.putExtra("Guest", 1);
        }
        startActivity(movieDetailIntent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    //not work yet
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("work", "work!!!");
        int id = item.getItemId();
        if (id == R.id.signout) {
            Log.d("work", "work!!!");
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent Signin = new Intent(this, SignIn.class);
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(Signin);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
