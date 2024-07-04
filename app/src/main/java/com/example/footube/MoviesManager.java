package com.example.footube;

import java.util.ArrayList;
import java.util.List;

public class MoviesManager {

    private static MoviesManager instance;
    private List<Movie> movies;

    private MoviesManager() {
        movies = new ArrayList<>();
    }

    public static synchronized MoviesManager getInstance() {
        if (instance == null) {
            instance = new MoviesManager();
        }
        return instance;
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
