package com.example.footube.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;
import com.example.footube.Repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> moviesLiveData;
    private LiveData<List<Movie>> movies;
    private LiveData<Movie> movie;

    public MovieViewModel() {
        repository = new MovieRepository();
        movies = repository.getAll();
        moviesLiveData = repository.getMoviesData();
//        movie = repository.getMovieData();
//        Feed = repository.getFeedData();
    }

    public void addMovie(Movie newMovie) {
        repository.addMovie(newMovie);
    }

    public void reload() {
        repository.reload();
    }

    public LiveData<List<Movie>> getMovieLiveData() {
        return movies;
    }

//    public void getvideosData() {
//        return this.Feed;
//    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }
}
