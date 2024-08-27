package com.example.footube.tasks;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.api.MovieAPI;
import com.example.footube.dao.MovieDao;

import java.util.List;

public class GetMoviesTask extends AsyncTask<Void, Void, Void> {
    private MutableLiveData<List<Movie>> movieListData;
    private MovieDao dao;
    private final MovieAPI api;

    public GetMoviesTask(MutableLiveData<List<Movie>> movieListData, MovieDao dao, MovieAPI api) {
        this.movieListData = movieListData;
        this.dao = dao;
        this.api = api;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // Fetch movies from the API
            api.get();
//            List<Movie> movies = api.get();
            // Update the database with the new movies
//            if (movies != null && !movies.isEmpty()) {
//                dao.insertAll(movies);
//            }

            // Post the fetched movies to the LiveData object
//            movieListData.postValue(movies);

        } catch (Exception e) {
            // Handle any errors during fetching, parsing, or updating the database
            e.printStackTrace();
        }
        return null;
    }
}

