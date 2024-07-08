package com.example.footube;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MoviesManager {

    private static MoviesManager instance;
    private List<Movie> movies;
    private Context context;

    private MoviesManager(Context context) {
        this.context = context;
        movies = new ArrayList<>();
        AddDefaultMovies();
    }

    public static synchronized MoviesManager getInstance(Context context) {
        if (instance == null) {
            instance = new MoviesManager(context);
        }
        return instance;
    }

    private void AddDefaultMovies (){
        String movie1 = readTextFileFromRaw(R.raw.movie1);
        String movie1p = readTextFileFromRaw(R.raw.movie1p);
        Movie m1 = new Movie("ba", "meat1", "interesting", "beef", movie1);
        m1.setMovieImage(movie1p);
        this.movies.add(m1);


        String movie2 = readTextFileFromRaw(R.raw.movie2);
        String movie2p = readTextFileFromRaw(R.raw.movie2p);
        Movie m2 = new Movie("ba", "meat2", "interesting", "beef", movie2);
        m2.setMovieImage(movie2p);
        this.movies.add(m2);


        String movie3 = readTextFileFromRaw(R.raw.movie3);
        String movie3p = readTextFileFromRaw(R.raw.movie3p);
        Movie m3 = new Movie("ba", "meat3", "interesting", "beef", movie3);
        m3.setMovieImage(movie3p);
        this.movies.add(m3);


        String movie4= readTextFileFromRaw(R.raw.movie4);
        String movie4p = readTextFileFromRaw(R.raw.movie4p);
        Movie m4 = new Movie("ba", "meat4", "interesting", "beef", movie4);
        m4.setMovieImage(movie4p);
        this.movies.add(m4);


        String movie5= readTextFileFromRaw(R.raw.movie5);
        String movie5p = readTextFileFromRaw(R.raw.movie5p);
        Movie m5 = new Movie("ba", "meat5", "interesting", "beef", movie5);
        m5.setMovieImage(movie5p);
        this.movies.add(m5);


        String movie6= readTextFileFromRaw(R.raw.movie6);
        String movie6p = readTextFileFromRaw(R.raw.movie6p);
        Movie m6 = new Movie("ba", "meat6", "interesting", "beef", movie6);
        m6.setMovieImage(movie6p);
        this.movies.add(m6);


        String movie7= readTextFileFromRaw(R.raw.movie7);
        String movie7p = readTextFileFromRaw(R.raw.movie7p);
        Movie m7 = new Movie("ba", "meat7", "interesting", "beef", movie7);
        m7.setMovieImage(movie7p);
        this.movies.add(m7);


        String movie8= readTextFileFromRaw(R.raw.movie8);
        String movie8p = readTextFileFromRaw(R.raw.movie8p);
        Movie m8 = new Movie("ba", "meat8", "interesting", "beef", movie8);
        m8.setMovieImage(movie8p);
        this.movies.add(m8);


        String movie9= readTextFileFromRaw(R.raw.movie9);
        String movie9p = readTextFileFromRaw(R.raw.movie9p);
        Movie m9 = new Movie("ba", "meat9", "interesting", "beef", movie9);
        m9.setMovieImage(movie9p);
        this.movies.add(m9);


        String movie10= readTextFileFromRaw(R.raw.movie10);
        String movie10p = readTextFileFromRaw(R.raw.movie10p);
        Movie m10 = new Movie("ba", "meat10", "interesting", "beef", movie10);
        m10.setMovieImage(movie10p);
        this.movies.add(m10);
    }


    public String readTextFileFromRaw(int resId) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }

    public void removeMovie(int position) {
        if (position >= 0 && position < movies.size()) {
            movies.remove(position);
        }
    }

    public Movie getMovie(int position) {
        if (position >= 0 && position < movies.size()) {
            return movies.get(position);
        }
        return null;
    }

    public Movie findMovieByName(String name) {
        for (Movie movie : movies) {
            if (movie.getName().equalsIgnoreCase(name)) {
                return movie;
            }
        }
        return null;
    }

    public void UpdateMovie(Movie m, Movie mnew){
        Movie movie = findMovieByName(m.getName());
        movie.SetMovie(mnew);
    }

    public void addCommentToMovie(String movieName, Comment comment) {
        Movie movie = findMovieByName(movieName);
        if (movie != null) {
            movie.AddComment(comment);
        }
    }

    public int getMoviesCount() {
        return movies.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MoviesManager{");
        sb.append("movies=[");
        for (Movie movie : movies) {
            sb.append(movie.toString()).append(", ");
        }
        if (!movies.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
