package com.example.footube.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.ViewModel.MovieViewModel;
import com.example.footube.ViewModel.UserViewModel;
import com.example.footube.listeners.MovieAdapter;
import com.example.footube.localDB.LoggedInUser;
import com.example.footube.R;
import com.example.footube.BasicClasses.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AddMovie extends AppCompatActivity {

    private static final int REQUEST_VIDEO_PICK = 1;
    private EditText editTextMovieName;
    private EditText editTextMovieDescription;
    private EditText editTextMovieCategory;
    private VideoView videoViewUploadedMovie;
    private Uri videoUri;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        //define movieadapter
//        List<Movie> movies = new ArrayList<>(); // An empty list or populated list of movies
//
//        MovieAdapter.OnMovieClickListener listener = new MovieAdapter.OnMovieClickListener() {
//            @Override
//            public void onMovieClick(int position) {
//                // Handle the movie click event
//                // For example, you might want to open the movie detail page
//                Movie clickedMovie = movies.get(position);
//                Toast.makeText(AddMovie.this, "Clicked on: " + clickedMovie.getName(), Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        adapter = new MovieAdapter(movies, listener);


        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
        User user = LoggedInUser.getInstance().getUser();

        TextView creator = findViewById(R.id.creator);
        creator.setText("Movie Creator: " + user.getUsername());
        creator.setGravity(Gravity.CENTER);

        //define view models
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        editTextMovieName = findViewById(R.id.editTextMovieName);
        editTextMovieDescription = findViewById(R.id.editTextMovieDescription);
        editTextMovieCategory = findViewById(R.id.editTextMovieCategory);
        videoViewUploadedMovie = findViewById(R.id.videoViewUploadedMovie);

        Button buttonUploadMovie = findViewById(R.id.buttonUploadMovie);
        buttonUploadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(v);
                chooseVideoFromGallery();
            }
        });

        Button buttonaddMovie = findViewById(R.id.buttonaddMovie);
        buttonaddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovieToManager();
            }
        });
    }

    private void chooseVideoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_VIDEO_PICK);
    }

    private void addMovieToManager() {
        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
        User user = LoggedInUser.getInstance().getUser();
        String username = user.getUsername();
        String movieName = editTextMovieName.getText().toString();
        String movieDescription = editTextMovieDescription.getText().toString();
        String movieCategory = editTextMovieCategory.getText().toString();

        if (movieName.isEmpty() || movieDescription.isEmpty() || movieCategory.isEmpty() || videoUri == null) {
            // Handle case where any field is empty or videoUri is null
            // Typically show a Toast or error message
            Toast.makeText(this, "Please fill all fields and upload a video.", Toast.LENGTH_SHORT).show();
        }else {
            String base64Video = videoUriToBase64(getContentResolver(), videoUri);
            // Create a Movie object with the entered details
            Movie newMovie = new Movie(user.getDisplayName(),username, movieName, movieDescription, movieCategory, base64Video, "");

            Bitmap thumbnail = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();

            try {
                // Use itemView's context to set data source
                retriever.setDataSource(this, videoUri);

                // Get the thumbnail
                thumbnail = retriever.getFrameAtTime();
            } catch (Exception e) {
                Log.e("AddMovie", "Failed to retrieve thumbnail for movie: " + movieName, e);
            } finally {
                try {
                    retriever.release();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            newMovie.setMovieImage(bitmapToBase64(thumbnail));

            movieViewModel.addMovie(newMovie);

            // Observe the LiveData to get the updated list of movies
            movieViewModel.getMovieLiveData().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> movies) {
                    Log.d("newMovies", movies.toString());
                    Toast.makeText(AddMovie.this, "Movie added successfully!", Toast.LENGTH_SHORT).show();

                    // Clear the input fields and reset the VideoView
                    editTextMovieName.setText("");
                    editTextMovieDescription.setText("");
                    editTextMovieCategory.setText("");
                    videoViewUploadedMovie.setVideoURI(null); // Clear the video

                    // Finish the activity after adding the movie
                    finish();
                }
            });

            movieViewModel.reload(); // Ensure data is reloaded after the movie is added

            //close the activity
            finish();
        }


    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void closeKeyboard(View view) {
        // Check if no view has focus
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        } else {
            Toast.makeText(this, "No view has focus", Toast.LENGTH_SHORT).show();
        }
    }

    public static String videoUriToBase64(ContentResolver contentResolver, Uri videoUri) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            // Open an InputStream from the URI
            inputStream = contentResolver.openInputStream(videoUri);

            if (inputStream == null) {
                return null;
            }

            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read the video file into the byte array output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convert the byte array output stream to a byte array
            byte[] videoBytes = byteArrayOutputStream.toByteArray();

            // Encode the byte array to Base64
            return Base64.encodeToString(videoBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close streams to avoid memory leaks
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_VIDEO_PICK) {
            if (data != null) {
                videoUri = data.getData();
                videoViewUploadedMovie.setVideoURI(videoUri);

                // Create a MediaController and set it to the VideoView
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoViewUploadedMovie);
                videoViewUploadedMovie.setMediaController(mediaController);

                videoViewUploadedMovie.start();
            }
        }
    }
}