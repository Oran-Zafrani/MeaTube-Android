package com.example.footube.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.ViewModel.MovieViewModel;
import com.example.footube.ViewModel.UserViewModel;
import com.example.footube.listeners.MovieAdapter;
import com.example.footube.localDB.LoggedInUser;
import com.example.footube.managers.MoviesManager;
import com.example.footube.R;
import com.example.footube.BasicClasses.User;
import com.example.footube.managers.UserManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MoviesList extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private static final int REQUEST_CODE_ADD_MOVIE = 1;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String PREF_DARK_MODE = "dark_mode";


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton sideMenuButton;
    private ImageButton signInButton;
    private ImageView userImage;
    private CardView userImageCard;
    private TextView userName;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private User user;
    private ImageButton SearchButton;
    private EditText SearchEditText;
    private UserViewModel userViewModel;
    private MovieViewModel movieViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);


        //set dark mode if relevant
        applyThemeBasedOnPreference();

        //create view models
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        movieViewModel.getMovies();
        movieViewModel.getMoviesLiveData().observe(this, movies -> {
            Log.d("adapters", movies.toString());
            adapter = new MovieAdapter((List<Movie>) movies, this);
            recyclerView.setAdapter(adapter);
        });

        adapter = new MovieAdapter(MoviesManager.getInstance(this).getMovies(),this);
        recyclerView.setAdapter(adapter);

        // Initialize the views
        signInButton = findViewById(R.id.signin);
        userImage = findViewById(R.id.user_image);
        userImageCard = findViewById(R.id.user_image_card);
        userName = findViewById(R.id.user_name);


        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        //user = (User) intent.getSerializableExtra("user");

        //get user from the server
        if (LoggedInUser.getInstance().getUser() != null)
            userViewModel.getUser(LoggedInUser.getInstance().getUser().getUsername());
        userViewModel.getUserLiveData().observe(this, userdata -> {
            Log.d("passwordOfTheUser", userdata.getDisplayName().toString());
            user = userdata;
            Log.d("passwordOfTheUser12", user.getPassword().toString());

            // Set visibility based on whether the user is present
            signInButton.setVisibility(View.GONE);
            userImage.setVisibility(View.VISIBLE);
            userImageCard.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            userName.setText(user.getDisplayName());
            setImage(user.getImage());

            //clickable to user image
            Intent UserMoviesListIntent = new Intent(this, UserProfile.class);
            UserMoviesListIntent.putExtra("movie_creator",LoggedInUser.getInstance().getUser().getUsername());

            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(UserMoviesListIntent);
                }
            });
        });



        // Load users from JSON
        //UserManager userManager = UserManager.getInstance();
        //userManager.loadUsersFromJSON(this);



//        if (user != null) {


//        } else {
            signInButton.setVisibility(View.VISIBLE);
            userImage.setVisibility(View.GONE);
            userImageCard.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInIntent = new Intent(MoviesList.this, SignIn.class);
                    startActivity(signInIntent);
                }
            });
//        }

        SearchButton = findViewById(R.id.searchbutton);
        SearchEditText = findViewById(R.id.searchedittext);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filter(SearchEditText.getText().toString());
            }
        });



        FloatingActionButton btnAddMovie = findViewById(R.id.btnAddMovie);
        btnAddMovie.setOnClickListener(view -> {
            try {
                // Create an Intent to start the new activity
                if (user != null) {
                    Intent addMovieIntent = new Intent(this, AddMovie.class);
//                    addMovieIntent.putExtra("user", user);
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
        if (LoggedInUser.thereIsUser()){
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
                    LoggedInUser.getInstance().deleteLoggedInUser();
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

        if (LoggedInUser.getInstance().getUser() != null)
            userViewModel.getUser(LoggedInUser.getInstance().getUser().getUsername());
        userViewModel.getUserLiveData().observe(this, userdata -> {
            MenuItem signOutItem = navigationView.getMenu().findItem(R.id.signout);
            signOutItem.setVisible(true);
        });


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

                // Update my data (e.g., notify your adapter of data changes)
                // myAdapter.notifyDataSetChanged();
            }
        }, 2000); // Simulate a delay
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update the adapter
        movieViewModel.getMovies();
        movieViewModel.getMoviesLiveData().observe(this, movies -> {
            Log.d("adapters", movies.toString());
            adapter = new MovieAdapter((List<Movie>) movies, this);
            recyclerView.setAdapter(adapter);
        });
        // Update the RecyclerView with the latest movies
        adapter.notifyDataSetChanged();
        adapter.filter("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // update the adapter
        movieViewModel.getMovies();
        movieViewModel.getMoviesLiveData().observe(this, movies -> {
            Log.d("adapters", movies.toString());
            adapter = new MovieAdapter((List<Movie>) movies, this);
            recyclerView.setAdapter(adapter);
        });

        if (requestCode == REQUEST_CODE_ADD_MOVIE && resultCode == RESULT_OK) {
            // Update the RecyclerView with the latest movies
            adapter.notifyDataSetChanged();
        }
    }

    private void toggleTheme() {
        // Access the SharedPreferences file
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Check if the system's default is dark mode
        boolean isSystemDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        // Retrieve the current theme preference or use the system default if not set
        boolean isDarkMode = preferences.getBoolean(PREF_DARK_MODE, isSystemDarkMode);

        // Toggle the theme
        if (isDarkMode) {
            // Set the theme to light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            // Save the new preference
            preferences.edit().putBoolean(PREF_DARK_MODE, false).apply();
        } else {
            // Set the theme to dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            // Save the new preference
            preferences.edit().putBoolean(PREF_DARK_MODE, true).apply();
        }

        // Recreate the activity to apply the new theme
        recreate();
    }

    private void applyThemeBasedOnPreference() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Check if the system's default is dark mode
        boolean isSystemDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        // Retrieve the current theme preference or use the system default if not set
        boolean isDarkMode = preferences.getBoolean(PREF_DARK_MODE, isSystemDarkMode);

        // Apply theme based on preference
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    public static Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException {
        if (base64Str != null){
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return null;
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onBackPressed() {
        if (user != null) {
            Intent Signin = new Intent(this, SignIn.class);
            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        LoggedInUser.getInstance().deleteLoggedInUser();
                        startActivity(Signin);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clear theme preference
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        preferences.edit().remove(PREF_DARK_MODE).apply();
    }
}
