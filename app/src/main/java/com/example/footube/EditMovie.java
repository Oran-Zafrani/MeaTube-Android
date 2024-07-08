package com.example.footube;

import static com.example.footube.AddMovie.bitmapToBase64;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditMovie extends AppCompatActivity {

    private static final int REQUEST_VIDEO_PICK = 1;
    private Movie movie;
    private MoviesManager movies;
    private User user;
    private String userName;
    private int position;
    private Uri videoUri;
    private EditText moviename;
    private EditText moviedecription;
    private EditText moviecategory;
    private TextView editor;
    private VideoView videoViewUploadedMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_movie);
        movies = MoviesManager.getInstance(this);
        position = getIntent().getIntExtra("movie_index", -1);
        movie = movies.getMovie(position);
        user = (User) getIntent().getSerializableExtra("user");
        userName = user.getUsername();
        user = UserManager.getInstance().getUser(userName);

        moviename = findViewById(R.id.editTextMovieName);
        moviedecription = findViewById(R.id.editTextMovieDescription);
        moviecategory = findViewById(R.id.editTextMovieCategory);
        editor = findViewById(R.id.editor);
        videoViewUploadedMovie = findViewById(R.id.videoViewUploadedMovie);
//
        moviename.setText(movie.getName());
        moviedecription.setText(movie.getDescription());
        moviecategory.setText(movie.getCategory());
        editor.setText("Editor: " + userName);
        setupVideoPlayer(movie.getMovieUri());

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

    private void addMovieToManager() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        String username = user.getUsername();
        String movieName = moviename.getText().toString();
        String movieDescription = moviedecription.getText().toString();
        String movieCategory = moviecategory.getText().toString();

        if (movieName.isEmpty() || movieDescription.isEmpty() || movieCategory.isEmpty()) {
            // Handle case where any field is empty or videoUri is null
            // Typically show a Toast or error message
            Toast.makeText(this, "Please fill all fields and upload a video.", Toast.LENGTH_SHORT).show();
            return;
        }

        String base64Video;
        if (videoUri != null){
            base64Video = videoUriToBase64(getContentResolver(), videoUri);
            Log.d("editmovie111", base64Video);
        }else {
            base64Video = movie.getMovieUri();
            Log.d("editmovie222", base64Video);
        }
        // Create a Movie object with the entered details
        Movie newMovie = new Movie(username, movieName, movieDescription, movieCategory, base64Video);

        Bitmap thumbnail = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        if (videoUri != null) {
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
        }else {
            thumbnail = base64ToBitmap(movie.GetImage());
        }

        newMovie.setMovieImage(bitmapToBase64(thumbnail));

        Log.d("update movie", newMovie.getName());

        // Add the movie to MoviesManager
        MoviesManager.getInstance(this).UpdateMovie(movie, newMovie);
        Toast.makeText(this, "Movie update successfully!", Toast.LENGTH_SHORT).show();

//        Log.d("new movie",MoviesManager.getInstance().toString());

        // Optionally, clear the input fields and reset the VideoView
        moviename.setText("");
        moviedecription.setText("");
        moviecategory.setText("");
        videoViewUploadedMovie.setVideoURI(null); // Clear the video

        // Provide feedback to the user (e.g., Toast message) that the movie was added successfully
        // Optionally navigate to another activity or perform additional actions
    }

    private void chooseVideoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_VIDEO_PICK);
    }

    private void setupVideoPlayer(String base64Video) {
        byte[] videoBytes = Base64.decode(base64Video, Base64.DEFAULT);
        File tempFile = null;
        try {
            tempFile = File.createTempFile("tempVideo", ".mp4", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(videoBytes);
            fos.close();
        } catch (IOException e) {
            Log.e("VideoPlayerActivity", "Error writing video to temp file", e);
            return;
        }

        if (tempFile != null) {
            Uri uri = Uri.fromFile(tempFile);
            videoViewUploadedMovie.setVideoURI(uri);

            // Create a MediaController and set it to the VideoView
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(videoView);
//            videoView.setMediaController(mediaController);

            CustomMediaController customMediaController = new CustomMediaController(this);
            customMediaController.setVideoView(videoViewUploadedMovie);
            customMediaController.setAnchorView(findViewById(R.id.frame)); // Ensure the anchor view is set to the FrameLayout
            videoViewUploadedMovie.setMediaController(customMediaController);

            videoViewUploadedMovie.start();
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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

    public static Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}