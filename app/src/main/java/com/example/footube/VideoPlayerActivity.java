package com.example.footube;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
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
    private ImageView likeButton;
    private ImageView unlikeButton;
    private TextView numberOfLikes;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private Movie movie;
    private MoviesManager movies;
    private int position;
    private User user;
    private String userName;
    private boolean isLiked = false;
    private boolean isUnliked = false;

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
        likeButton = findViewById(R.id.likeButton);
        unlikeButton = findViewById(R.id.unlikeButton);
        numberOfLikes = findViewById(R.id.number_of_likes);

        movies = MoviesManager.getInstance();
        position = getIntent().getIntExtra("movie_index", -1);
        user = (User) getIntent().getSerializableExtra("username");
        userName = user.getUsername();
        movie = movies.getMovie(position);

        if (movie != null) {
            setupVideoPlayer(movie.getMovieUri());
            videoTitle.setText(movie.getName());
            videoCreator.setText(movie.getCreator());
            videoDescription.setText(movie.getDescription());
            setupCommentsRecyclerView();
            numberOfLikes.setText(String.valueOf(movie.GetLikes()));
        }

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = editTextComment.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(userName, commentText);
                    movies.addCommentToMovie(movie.getName(), newComment);
                    commentsAdapter.notifyItemInserted(commentList.size() - 1);
                    editTextComment.setText("");
                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    isLiked = false;
                    likeButton.setImageResource(R.drawable.ic_thumb_up);
                    movie.setLikes(movie.getLikes() - 1);
                } else {
                    isLiked = true;
                    likeButton.setImageResource(R.drawable.ic_thumb_up_blue);
                    movie.setLikes(movie.GetLikes() + 1);
                    numberOfLikes.setTextColor(getResources().getColor(R.color.blue));
                    if (isUnliked) {
                        isUnliked = false;
                        unlikeButton.setImageResource(R.drawable.ic_thumb_down);
                        movie.setUnlikes(movie.getLikes() - 1);
                    }
                }
                numberOfLikes.setText(String.valueOf(movie.GetLikes()));
            }
        });

        unlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUnliked) {
                    isUnliked = false;
                    unlikeButton.setImageResource(R.drawable.ic_thumb_down);
                } else {
                    isUnliked = true;
                    unlikeButton.setImageResource(R.drawable.ic_thumb_down_fill_red);
                    movie.setUnlikes(movie.getLikes() + 1);
                    movie.setLikes(movie.getLikes() - 1);
                    if (isLiked) {
                        isLiked = false;
                        likeButton.setImageResource(R.drawable.ic_thumb_up);
                        movie.setLikes(movie.getLikes() - 1);
                    }
                }
//                numberOfLikes.setText(String.valueOf(movie.GetLikes()-1));
            }
        });
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
            videoView.setVideoURI(uri);

            // Create a MediaController and set it to the VideoView
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            Log.d("VideoPlayerActivity", "Playing video from temp file: " + uri.toString());
            videoView.start();
        }
    }

    private void setupCommentsRecyclerView() {
        commentList = movie.GetComments();
        commentsAdapter = new CommentsAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);
    }
}
