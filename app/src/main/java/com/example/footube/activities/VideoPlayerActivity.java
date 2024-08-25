package com.example.footube.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footube.BasicClasses.Comment;
import com.example.footube.listeners.CommentsAdapter;
import com.example.footube.designs.CustomMediaController;
import com.example.footube.BasicClasses.Movie;
import com.example.footube.listeners.MovieAdapter;
import com.example.footube.managers.MoviesManager;
import com.example.footube.R;
import com.example.footube.BasicClasses.User;
import com.example.footube.managers.UserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class VideoPlayerActivity extends AppCompatActivity implements CommentsAdapter.OnDeleteCommentListener, CommentsAdapter.OnEditCommentListener, MovieAdapter.OnMovieClickListener {

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
    private ImageView shareButton;
    private ImageView uploadUserImage;
    private Button beditmovie;
    private TextView numberOfLikes;
    private TextView numberOfUnlikes;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private Movie movie;
    private MoviesManager movies;
    private UserManager users;
    private int position;
    private int Views = 0;
    private int isGuest;
    private User loggedInUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView NumberOfComments;
    private MovieAdapter adapter;
    private String loggedInUserName;
    private TextView textNoComments;
    private ConstraintLayout commentsLayout;
    private ImageButton userImage;
    private boolean isLiked = false;
    private boolean isUnliked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Initialize the views
        videoView = findViewById(R.id.videoView);
        videoTitle = findViewById(R.id.video_title);
        videoCreator = findViewById(R.id.video_creator);
        videoDescription = findViewById(R.id.video_description);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        likeButton = findViewById(R.id.likeButton);
        unlikeButton = findViewById(R.id.unlikeButton);
        shareButton = findViewById(R.id.shareButton);
        numberOfLikes = findViewById(R.id.number_of_likes);
        numberOfUnlikes = findViewById(R.id.number_of_unlikes);
        beditmovie = findViewById(R.id.editmovie);
        TViews = findViewById(R.id.views);
        textNoComments = findViewById(R.id.NoComments);
        commentsLayout = findViewById(R.id.commentsSection);
        uploadUserImage = findViewById(R.id.uploader_image);
        NumberOfComments = findViewById(R.id.commentCountTextView);


        // Get the logged user data
        isGuest = getIntent().getIntExtra("Guest", -1);
        // Check if the user is a guest
        if (isGuest == 0){  // If the user is not a guest
            loggedInUserName = getIntent().getStringExtra("username");
            loggedInUser = UserManager.getInstance().getUser(loggedInUserName);
        }else if (isGuest == 1) { // If the user is a guest
            loggedInUser = new User("Guest","Guest", "", "");
            loggedInUserName = loggedInUser.getUsername();
        }

        // Get the movie data
        movies = MoviesManager.getInstance(this);
        position = getIntent().getIntExtra("movie_index", -1);
        movie = movies.getMovie(position);
        movie.AddView();
        TViews.setText(MovieAdapter.formatViews(movie.getViews()) + " Views");

        if (movie != null) {
            setupVideoPlayer(movie.getMovieUri());
            videoTitle.setText(movie.getName());
            videoCreator.setText(movie.getChannel());
            videoDescription.setText(movie.getDescription());
            setupCommentsRecyclerView();
            numberOfLikes.setText(MovieAdapter.formatViews(movie.getLikes()));
            numberOfUnlikes.setText(MovieAdapter.formatViews(movie.getUnlikes()));
            uploadUserImage.setImageBitmap(MoviesList.base64ToBitmap(UserManager.getInstance().getUser(movie.getCreator()).getImage()));
            if (loggedInUser != null){
                PrivateLikesLogic();
            }
            TextView uploadTimeTextView = findViewById(R.id.upload_time);
            String relativeTime = movie.getRelativeTime();
            uploadTimeTextView.setText(relativeTime);
            if (!Objects.equals(loggedInUserName, movie.getCreator())){
                beditmovie.setVisibility(View.GONE);
            }
        }




        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuest != 0){
                    Toast.makeText(VideoPlayerActivity.this, "You need to log in first before you comment.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String commentText = editTextComment.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(loggedInUser.getDisplayName(),loggedInUserName, commentText,0,0, loggedInUser.getImage());
                    movies.addCommentToMovie(movie.getName(), newComment);
                    commentsAdapter.notifyItemInserted(commentList.size() - 1);
                    closeKeyboard(v);
                    editTextComment.setText("");
                }
            }
        });

        //Define the attrs
        TypedValue typedValue_Unlike = new TypedValue();
        TypedValue typedValue_Like = new TypedValue();
        TypedValue typedValue_Unlike_Like_Color_Text = new TypedValue();
        getTheme().resolveAttribute(R.attr.Unlike, typedValue_Unlike, true);
        getTheme().resolveAttribute(R.attr.Like, typedValue_Like, true);
        getTheme().resolveAttribute(R.attr.Unlike_Like_Color_Text, typedValue_Unlike_Like_Color_Text, true);


        //go to video of the user
        userImage = findViewById(R.id.uploader_image);
        Intent UserMoviesListIntent = new Intent(this, userVideoList.class);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UserMoviesListIntent);
            }
        });


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuest != 1) {
                    if (isLiked) {
                        isLiked = false;
                        likeButton.setImageResource(typedValue_Like.resourceId);
                        numberOfLikes.setTextColor(typedValue_Unlike_Like_Color_Text.data);
                        numberOfUnlikes.setTextColor(typedValue_Unlike_Like_Color_Text.data);
                        if (loggedInUser.searchlike(movie)) {
                            loggedInUser.RemoveLike(movie);
                        }
                        loggedInUser.RemoveLike(movie);
                        movie.setLikes(movie.getLikes() - 1);
                    } else {
                        isLiked = true;
                        likeButton.setImageResource(R.drawable.ic_thumb_up_blue);
                        movie.setLikes(movie.getLikes() + 1);
                        numberOfLikes.setTextColor(getResources().getColor(R.color.blue));
                        numberOfUnlikes.setTextColor(typedValue_Unlike_Like_Color_Text.data);
                        if (loggedInUser.searchunlike(movie)) {
                            loggedInUser.RemoveUnLike(movie);
                        }
                        loggedInUser.AddLike(movie);
                        if (isUnliked) {
                            isUnliked = false;
                            unlikeButton.setImageResource(typedValue_Unlike.resourceId);
                            movie.setUnlikes(movie.getUnlikes() - 1);
                        }
                    }
                    numberOfLikes.setText(MovieAdapter.formatViews(movie.getLikes()));
                    numberOfUnlikes.setText(MovieAdapter.formatViews(movie.getUnlikes()));
                }
                else {
                    Toast.makeText(VideoPlayerActivity.this, "You need to sign in to like", Toast.LENGTH_SHORT).show();
                }
            }
        });

        unlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuest != 1) {
                    if (isUnliked) {
                        isUnliked = false;
                        unlikeButton.setImageResource(typedValue_Unlike.resourceId);
                        numberOfLikes.setTextColor(typedValue_Unlike_Like_Color_Text.data);
                        numberOfUnlikes.setTextColor(typedValue_Unlike_Like_Color_Text.data);
                        if (loggedInUser.searchunlike(movie)) {
                            loggedInUser.RemoveUnLike(movie);
                        }
                        loggedInUser.RemoveUnLike(movie);
                        movie.setUnlikes(movie.getUnlikes() - 1);
                        numberOfUnlikes.setText(MovieAdapter.formatViews(movie.getUnlikes()));
                    } else {
                        isUnliked = true;
                        unlikeButton.setImageResource(R.drawable.ic_thumb_down_fill_red);
                        numberOfLikes.setTextColor(typedValue_Unlike_Like_Color_Text.data);
                        numberOfUnlikes.setTextColor(getResources().getColor(R.color.red));
                        movie.setUnlikes(movie.getUnlikes() + 1);
                        if (loggedInUser.searchlike(movie)) {
                            loggedInUser.RemoveLike(movie);
                        }
                        loggedInUser.AddUnLike(movie);
                        if (isLiked) {
                            isLiked = false;
                            likeButton.setImageResource(typedValue_Like.resourceId);
                            movie.setLikes(movie.getLikes() - 1);
                        }
                    }
                    numberOfLikes.setText(MovieAdapter.formatViews(movie.getLikes()));
                    numberOfUnlikes.setText(MovieAdapter.formatViews(movie.getUnlikes()));
                }
                else {
                    Toast.makeText(VideoPlayerActivity.this, "You need to sign in to unlike", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Base URL
                String baseUrl = "http://localhost:3000/watch/";

                // ID of the video
                int videoId = movie.getId();

                // Full URL
                String videoUrl = baseUrl + videoId;

                // Share the video
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome video: " + videoUrl);
                startActivity(Intent.createChooser(shareIntent, "Share video"));
            }
        });

        beditmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditMovieInIntent = new Intent(VideoPlayerActivity.this, EditMovie.class);
                EditMovieInIntent.putExtra("movie_index", position);
                EditMovieInIntent.putExtra("user", loggedInUser);
                finish();
                startActivity(EditMovieInIntent);
            }
        });

        //upload comments
        UploadMovies();

        //If no comments - to continue
//        if (commentsAdapter.getItemCount() == 0){
//            commentsLayout.setVisibility(View.GONE);
//            textNoComments.setVisibility(View.VISIBLE);
//        }else {
//            commentsLayout.setVisibility(View.VISIBLE);
//            textNoComments.setVisibility(View.GONE);
//        }
    }

    private void UploadMovies(){
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        adapter = new MovieAdapter(MoviesManager.getInstance(this).getMovies(),this);
        recyclerView.setAdapter(adapter);

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the refresh operation
                refreshData();
            }
        });
    }

    private void refreshData() {
        // Simulate a refresh operation (e.g., fetch new data)
        // After the operation is complete, call setRefreshing(false) to stop the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //cancel the filter (clean the search)
                adapter.filter("");

                // Stop the refresh animation
                swipeRefreshLayout.setRefreshing(false);


                // Update my data (e.g., notify your adapter of data changes)
                // myAdapter.notifyDataSetChanged();
            }
        }, 2000); // Simulate a delay
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

            CustomMediaController customMediaController = new CustomMediaController(this);
            customMediaController.setVideoView(videoView);
            customMediaController.setAnchorView(findViewById(R.id.frame)); // Ensure the anchor view is set to the FrameLayout
            videoView.setMediaController(customMediaController);

            videoView.start();
        }
    }

    private void setupCommentsRecyclerView() {
        commentList = movie.GetComments();
        commentsAdapter = new CommentsAdapter(loggedInUserName,this, commentList, this, this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        //set number of comments
        NumberOfComments.setText(commentList.size()+ " Comments");
    }

    private void PrivateLikesLogic(){
        if (loggedInUser.searchlike(movie)){
            Log.d("videoplayeractivity", "work!");
            isLiked = true;
            likeButton.setImageResource(R.drawable.ic_thumb_up_blue);
            numberOfLikes.setTextColor(getResources().getColor(R.color.blue));
        }
        if (loggedInUser.searchunlike(movie)){
            isUnliked = true;
            Log.d("videoplayeractivity", "work2!");
            unlikeButton.setImageResource(R.drawable.ic_thumb_down_fill_red);
            numberOfUnlikes.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void onDeleteComment(int position) {
        Comment commentToDelete = commentList.get(position);
        commentList.remove(position);
        commentsAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onEditComment(int position, String newComment) {
        movie.GetComments().get(position).setComment(newComment);
        commentsAdapter.notifyItemChanged(position);
    }
    public void hideineditcomment(){
        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        beditmovie = findViewById(R.id.editmovie);

        beditmovie.setVisibility(View.GONE);
        buttonAddComment.setVisibility(View.GONE);
        editTextComment.setVisibility(View.GONE);
    }

    public void visibleineditcomment(){
        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        beditmovie = findViewById(R.id.editmovie);

        beditmovie.setVisibility(View.VISIBLE);
        buttonAddComment.setVisibility(View.VISIBLE);
        editTextComment.setVisibility(View.VISIBLE);
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

    public void openKeyboard(View view) {
        if (view != null) {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            Toast.makeText(this, "View is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMovieClick(int position) {
        Intent movieDetailIntent = new Intent(this, VideoPlayerActivity.class);
        movieDetailIntent.putExtra("movie_index", position);
        if(loggedInUser != null && !Objects.equals(loggedInUser.getUsername(), "Guest")){
            movieDetailIntent.putExtra("username", loggedInUser.getUsername());
            movieDetailIntent.putExtra("Guest", 0);
        }else {
            movieDetailIntent.putExtra("Guest", 1);
        }
        startActivity(movieDetailIntent);
        finish();
    }
}
