package com.example.footube;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getName());
        holder.descriptionTextView.setText(movie.getDescription());
        holder.genreTextView.setText(movie.getCategory());
        holder.creatorTextView.setText(movie.getCreator());  // Set the creator text

        // Generate a thumbnail from the video URI
        Bitmap thumbnail = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            // Use itemView's context to set data source
            retriever.setDataSource(holder.itemView.getContext(), Uri.parse(movie.getMovieUri()));

            // Get the thumbnail
            thumbnail = retriever.getFrameAtTime();
        } catch (Exception e) {
            Log.e("MovieAdapter", "Failed to retrieve thumbnail for movie: " + movie.getName(), e);
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Set the thumbnail to the ImageView
        if (thumbnail != null) {
            holder.movieImageView.setImageBitmap(thumbnail);
        } else {
            holder.movieImageView.setImageResource(R.drawable.ic_launcher_background);  // Placeholder image
        }
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView genreTextView;
        TextView creatorTextView;  // Add a TextView for the creator
        ImageView movieImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title);
            descriptionTextView = itemView.findViewById(R.id.movie_description);
            genreTextView = itemView.findViewById(R.id.movie_category);
            creatorTextView = itemView.findViewById(R.id.movie_creator);  // Initialize the creator TextView
            movieImageView = itemView.findViewById(R.id.movie_image);
        }
    }
}
