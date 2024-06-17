package com.example.footube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    }

    private void chooseVideoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_VIDEO_PICK);
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
