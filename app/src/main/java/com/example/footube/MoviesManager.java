package com.example.footube;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
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
        String fileContent = readTextFileFromRaw(R.raw.movie1);
        Movie m1 = new Movie("ba", "meat1", "interesting", "beef", fileContent);
        m1.setMovieImage("");
        this.movies.add(m1);
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

//    public static String convertMp4ToBase64(String filePath) throws IOException {
//        File file = new File(filePath);
//        FileInputStream fileInputStream = new FileInputStream(file);
//        byte[] bytes = new byte[(int) file.length()];
//        fileInputStream.read(bytes);
//        fileInputStream.close();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return Base64.getEncoder().encodeToString(bytes);
//        }
//        return filePath;
//    }


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
