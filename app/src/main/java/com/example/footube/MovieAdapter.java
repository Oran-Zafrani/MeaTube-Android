package com.example.footube;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static List<Movie> movies;
    private List<Movie> filteredMovies;
    private OnMovieClickListener onMovieClickListener;

    public interface OnMovieClickListener {
        void onMovieClick(int position);
    }

    public MovieAdapter(List<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.filteredMovies = new ArrayList<>(movies);
        this.onMovieClickListener = listener;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view, onMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
//        Movie movie = movies.get(position);
        Movie movie = filteredMovies.get(position);
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
//            Log.e("MovieAdapter", "Failed to retrieve thumbnail for movie: " + movie.getName(), e);
            Log.d("MovieAdapter", "Failed to retrieve thumbnail");
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Set the thumbnail to the ImageView
        if (thumbnail != null) {
            //bitmapToBase64(thumbnail);
            holder.movieImageView.setImageBitmap(thumbnail);
        } else {
            //Bitmap image =
            holder.movieImageView.setImageBitmap(base64ToBitmap(movie.GetImage()));  // Placeholder image
//            Log.d("wtf", Uri.parse(movie.getMovieUri()).toString());
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    @Override
    public int getItemCount() {
//        return movies.size();
        return filteredMovies.size();
    }

    public void filter(String query) {
        if (filteredMovies == null) {
            filteredMovies = new ArrayList<>(); // Initialize filteredMovies if null
        }
        filteredMovies.clear();
        if (query.isEmpty()) {
            filteredMovies.addAll(movies);
        } else {
            for (Movie movie : movies) {
                if (movie.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredMovies.add(movie);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView genreTextView;
        TextView creatorTextView;  // Add a TextView for the creator
        ImageView movieImageView;

        MovieViewHolder(View itemView, final OnMovieClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title);
            descriptionTextView = itemView.findViewById(R.id.movie_description);
            genreTextView = itemView.findViewById(R.id.movie_category);
            creatorTextView = itemView.findViewById(R.id.movie_creator);  // Initialize the creator TextView
            movieImageView = itemView.findViewById(R.id.movie_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onMovieClick(position);
                    }
                }
            });
        }
    }
}
