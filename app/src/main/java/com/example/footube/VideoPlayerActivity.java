package com.example.footube;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView videoTitle;
    private TextView videoCreator;
    private TextView videoDescription;
    private RecyclerView commentsRecyclerView;
    private EditText editTextComment;
    private Button buttonAddComment;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private Movie movie; // Add a field to store the movie object
    private MoviesManager movies; // Add a field to store the movie object
    private int position;
    private User user;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        videoTitle = findViewById(R.id.video_title);
        videoCreator = findViewById(R.id.video_creator);
        videoDescription = findViewById(R.id.video_description);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);

        movies = MoviesManager.getInstance();
        position = (int) getIntent().getSerializableExtra("movie_index");
        user = ((User) getIntent().getSerializableExtra("username"));
        userName = user.getUsername();
        // Retrieve the movie object from the Intent
        movie = movies.getMovie(position);

        if (movie != null) {
            setupVideoPlayer(movie.getMovieUri());
            videoTitle.setText(movie.getName());
            videoCreator.setText(movie.getCreator());
            videoDescription.setText(movie.getDescription());

            setupCommentsRecyclerView();
        }

        // Add a new comment
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = editTextComment.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(userName, commentText);
                    movies.addCommentToMovie(movie.getName(), newComment); // Add to movie
//                    Log.d("movie123", movies.getMovie(position).toString());
                    commentsAdapter.notifyItemInserted(commentList.size() - 1);
                    editTextComment.setText("");
                }
            }
        });
    }

//    private void setupVideoPlayer(String videoUri) {
//        Uri uri = Uri.parse(videoUri);
//        videoView.setVideoURI(uri);
//        Log.d("URI: ", uri.toString());
//        videoView.start();
//    }

    private void setupVideoPlayer(String base64Video) {
        // Decode Base64 string to byte array
        byte[] videoBytes = Base64.decode(base64Video, Base64.DEFAULT);

        // Write the byte array to a temporary file
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

        // Set the VideoView to play the video from the temporary file
        if (tempFile != null) {
            Uri uri = Uri.fromFile(tempFile);
            videoView.setVideoURI(uri);
            Log.d("VideoPlayerActivity", "Playing video from temp file: " + uri.toString());
            videoView.start();
        }
    }


    private void setupCommentsRecyclerView() {
        // Retrieve comments from the movie object
        commentList = movie.GetComments();
        commentsAdapter = new CommentsAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);
    }
}
