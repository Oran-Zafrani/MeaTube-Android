package com.example.footube;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AddMovie extends AppCompatActivity {

    private EditText editTextMovieName;
    private EditText editTextMovieDescription;
    private ImageView imageViewUploadedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        editTextMovieName = findViewById(R.id.editTextMovieName);
        editTextMovieDescription = findViewById(R.id.editTextMovieDescription);
        imageViewUploadedMovie = findViewById(R.id.imageViewUploadedMovie);

        Button buttonUploadMovie = findViewById(R.id.buttonUploadMovie);
        buttonUploadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload logic here
                // Replace placeholder image with uploaded movie image
                imageViewUploadedMovie.setImageResource(R.drawable.beef);
            }
        });
    }
}
