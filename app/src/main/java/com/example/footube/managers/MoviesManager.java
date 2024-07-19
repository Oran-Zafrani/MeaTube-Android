package com.example.footube.managers;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.footube.BasicClasses.Comment;
import com.example.footube.BasicClasses.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoviesManager {

    private static MoviesManager instance;
    private List<Movie> movies;
    private Context context;

    private MoviesManager(Context context) {
        this.context = context;
        movies = new ArrayList<>();
        loadMoviesFromJSON(context);
    }

    public static synchronized MoviesManager getInstance(Context context) {
        if (instance == null) {
            instance = new MoviesManager(context);
        }
        return instance;
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


    // Load movies from JSON file
    public void loadMoviesFromJSON(Context context) {
        try {
            InputStream is = context.getAssets().open("Feed.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Type movieListType = new TypeToken<List<Movie>>() {}.getType();
            List<Movie> movies = gson.fromJson(json, movieListType);

            String moviephoto;
            String Video;
            String commenterimage;
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                // Dynamically read the raw resource file based on the movie position


                int resourceId = context.getResources().getIdentifier(movie.getPreviewImage(), "raw", context.getPackageName());
                if (resourceId != 0) {
                    moviephoto = readTextFileFromRaw(context, resourceId);
                    movie.setMovieImage(moviephoto);
                }

                resourceId = context.getResources().getIdentifier(movie.getMovieUri(), "raw", context.getPackageName());
                if (resourceId != 0) {
                    Video = readTextFileFromRaw(context, resourceId);
                    movie.setMovieUri(Video);
                }

                if (movie.GetComments() != null){
                    for (int j = 0; j < movie.GetComments().size(); j++) {
                        resourceId = context.getResources().getIdentifier(movie.GetComments().get(j).getUserImage(), "raw", context.getPackageName());
                        if (resourceId != 0) {
                            commenterimage = readTextFileFromRaw(context, resourceId);
                            movie.GetComments().get(j).setUserImage(commenterimage);
                        }
                    }
                }

                addMovie(movie);
            }
        } catch (IOException ex) {
            Log.e("MoviesManager", "Error reading Feed.json", ex);
        }
    }

    public static String readTextFileFromRaw(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

}
