package com.example.footube;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddMovie extends AppCompatActivity {

    private static final int REQUEST_VIDEO_PICK = 1;

    private EditText editTextMovieName;
    private EditText editTextMovieDescription;
    private EditText editTextMovieCategory;
    private VideoView videoViewUploadedMovie;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        TextView creator = findViewById(R.id.creator);
        creator.setText("Movie Creator: " + user.getUsername());
        creator.setGravity(Gravity.CENTER);


        editTextMovieName = findViewById(R.id.editTextMovieName);
        editTextMovieDescription = findViewById(R.id.editTextMovieDescription);
        editTextMovieCategory = findViewById(R.id.editTextMovieCategory);
        videoViewUploadedMovie = findViewById(R.id.videoViewUploadedMovie);

        Button buttonUploadMovie = findViewById(R.id.buttonUploadMovie);
        buttonUploadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallery();
            }
        });

        Button buttonaddMovie = findViewById(R.id.buttonaddMovie);
        buttonaddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovieToManager();
                finish();
            }
        });
    }

    private void chooseVideoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_VIDEO_PICK);
    }

    private void addMovieToManager() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        String username = user.getUsername();
        String movieName = editTextMovieName.getText().toString();
        String movieDescription = editTextMovieDescription.getText().toString();
        String movieCategory = editTextMovieCategory.getText().toString();

        if (movieName.isEmpty() || movieDescription.isEmpty() || movieCategory.isEmpty() || videoUri == null) {
            // Handle case where any field is empty or videoUri is null
            // Typically show a Toast or error message
            Toast.makeText(this, "Please fill all fields and upload a video.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Movie object with the entered details
        Movie newMovie = new Movie(username, movieName, movieDescription, movieCategory, videoUri.toString());

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

        // Add the movie to MoviesManager
        MoviesManager.getInstance().addMovie(newMovie);
        Toast.makeText(this, "Movie added successfully!", Toast.LENGTH_SHORT).show();

        Log.d("new movie",MoviesManager.getInstance().toString());

        // Optionally, clear the input fields and reset the VideoView
        editTextMovieName.setText("");
        editTextMovieDescription.setText("");
        editTextMovieCategory.setText("");
        videoViewUploadedMovie.setVideoURI(null); // Clear the video

        // Provide feedback to the user (e.g., Toast message) that the movie was added successfully
        // Optionally navigate to another activity or perform additional actions
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_VIDEO_PICK) {
            if (data != null) {
                videoUri = data.getData();
                videoViewUploadedMovie.setVideoURI(videoUri);
                videoViewUploadedMovie.start();
            }
        }
    }
}