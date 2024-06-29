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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoPlayerActivity extends AppCompatActivity implements CommentsAdapter.OnDeleteCommentListener, CommentsAdapter.OnEditCommentListener {

    private VideoView videoView;
    private TextView videoTitle;
    private TextView videoCreator;
    private TextView videoDescription;
    private TextView TViews;
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
    private UserManager users;
    private int position;
    private int Views = 0;
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
        TViews = findViewById(R.id.views);

        movies = MoviesManager.getInstance();
        position = getIntent().getIntExtra("movie_index", -1);
        user = (User) getIntent().getSerializableExtra("username");
        userName = user.getUsername();
        user = UserManager.getInstance().getUser(userName);
        movie = movies.getMovie(position);
        movie.AddView();
        TViews.setText(movie.getViews() + " Views");

        if (movie != null) {
            setupVideoPlayer(movie.getMovieUri());
            videoTitle.setText(movie.getName());
            videoCreator.setText(movie.getCreator());
            videoDescription.setText(movie.getDescription());
            setupCommentsRecyclerView();
            numberOfLikes.setText(String.valueOf(movie.getLikes()));
        }

        numberOfLikes.setTextColor(getResources().getColor(R.color.black));
        PrivateLikesLogic();

        TextView uploadTimeTextView = findViewById(R.id.upload_time);
        Date uploadDate = movie.GetUploadTime();
        String relativeTime = getRelativeTime(uploadDate);
        uploadTimeTextView.setText(relativeTime);

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
                    numberOfLikes.setTextColor(getResources().getColor(R.color.black));
                    if(user.searchlike(movie)){
                        user.RemoveLike(movie);
                    }
                    user.RemoveLike(movie);
                    movie.setLikes(movie.getLikes() - 1);
                } else {
                    isLiked = true;
                    likeButton.setImageResource(R.drawable.ic_thumb_up_blue);
                    movie.setLikes(movie.getLikes() + 1);
                    numberOfLikes.setTextColor(getResources().getColor(R.color.blue));
                    if(user.searchunlike(movie)){
                        user.RemoveUnLike(movie);
                    }
                    user.AddLike(movie);
                    if (isUnliked) {
                        isUnliked = false;
                        unlikeButton.setImageResource(R.drawable.ic_thumb_down);
                        movie.setUnlikes(movie.getUnlikes() - 1);
                    }
                }
                numberOfLikes.setText(String.valueOf(movie.getLikes()));
            }
        });


//        DeleteComment = findViewById(R.id.deleteCommentTextView);
//        DeleteComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        unlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUnliked) {
                    isUnliked = false;
                    unlikeButton.setImageResource(R.drawable.ic_thumb_down);
                    numberOfLikes.setTextColor(getResources().getColor(R.color.black));
                    if(user.searchunlike(movie)){
                        user.RemoveUnLike(movie);
                    }
                    user.RemoveUnLike(movie);
                    movie.setUnlikes(movie.getUnlikes() - 1);
                } else {
                    isUnliked = true;
                    unlikeButton.setImageResource(R.drawable.ic_thumb_down_fill_red);
                    numberOfLikes.setTextColor(getResources().getColor(R.color.black));
                    movie.setUnlikes(movie.getUnlikes() + 1);
                    if(user.searchlike(movie)){
                        user.RemoveLike(movie);
                    }
                    user.AddUnLike(movie);
                    if (isLiked) {
                        isLiked = false;
                        likeButton.setImageResource(R.drawable.ic_thumb_up);
                        movie.setLikes(movie.getLikes() - 1);
                    }
                }
                numberOfLikes.setText(String.valueOf(movie.getLikes()));
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

            videoView.start();
        }
    }

    private void setupCommentsRecyclerView() {
        commentList = movie.GetComments();
        commentsAdapter = new CommentsAdapter(commentList, this, this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);
    }

    private void PrivateLikesLogic(){
        if (user.searchlike(movie)){
            Log.d("videoplayeractivity", "work!");
            isLiked = true;
            likeButton.setImageResource(R.drawable.ic_thumb_up_blue);
            numberOfLikes.setTextColor(getResources().getColor(R.color.blue));
        }
        if (user.searchunlike(movie)){
            isUnliked = true;
            Log.d("videoplayeractivity", "work2!");
            unlikeButton.setImageResource(R.drawable.ic_thumb_down_fill_red);
        }
    }

    @Override
    public void onDeleteComment(int position) {
        Comment commentToDelete = commentList.get(position);
//        movies.removeCommentFromMovie(movie.getName(), commentToDelete);
        commentList.remove(position);
        commentsAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onEditComment(int position, String newComment) {
        movie.GetComments().get(position).setComment(newComment);
        commentsAdapter.notifyItemChanged(position);
    }

    public static String getRelativeTime(Date uploadDate) {
        long uploadTime = uploadDate.getTime();
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - uploadTime;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long days = TimeUnit.MILLISECONDS.toDays(duration);

        if (seconds < 60) {
            return seconds == 1 ? "1 second ago" : seconds + " seconds ago";
        } else if (minutes < 60) {
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else if (hours < 24) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else if (days < 30) {
            return days == 1 ? "1 day ago" : days + " days ago";
        } else if (days < 365) {
            long months = days / 30;
            return months == 1 ? "1 month ago" : months + " months ago";
        } else {
            long years = days / 365;
            return years == 1 ? "1 year ago" : years + " years ago";
        }
    }
}
