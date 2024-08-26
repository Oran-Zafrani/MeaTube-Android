package com.example.footube.ViewModel;

import androidx.lifecycle.LiveData;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;
import com.example.footube.Repository.MovieRepository;

import java.util.List;

public class MovieViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> Movies;
    private LiveData<List<Movie>> Feed;
    private LiveData<Movie> movie;

    public MovieViewModel() {
        repository = new MovieRepository();
//        Movies = repository.getAll();
//        movie = repository.getMovieData();
//        Feed = repository.getFeedData();
    }

    public void addMovie(Movie newMovie) {
        repository.addMovie(newMovie);
    }
}
