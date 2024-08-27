package com.example.footube.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;
import com.example.footube.MyApplication;
import com.example.footube.api.MovieAPI;
import com.example.footube.dao.MovieDao;
import com.example.footube.localDB.AppDB;
import com.example.footube.tasks.GetMoviesTask;

import java.util.LinkedList;
import java.util.List;

public class MovieRepository {
    private MovieDao dao;
    private MovieListData movieListData;
    private MutableLiveData<List<Movie>> moviesData = new MutableLiveData<>();
    private MutableLiveData<Movie> movieMutableLiveData;
    private MovieAPI api;
    private MutableLiveData<List<Movie>> FeedData;

    public MovieRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "postDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        this.dao = db.movieDao();
        movieListData = new MovieListData();
        movieMutableLiveData = new MutableLiveData<>();
        FeedData = new MutableLiveData<>();
        api = new MovieAPI(movieListData, dao);
    }

    public void addMovie(Movie newMovie) {
        api.addMovie(newMovie);
    }

    public void reload() {
        new GetMoviesTask(movieListData, dao,api).execute();
    }

    public LiveData<List<Movie>> getAll() {
        return movieListData;
    }

    public void getMovies() {
        api.getMovies();
    }

    public LiveData<List<Movie>> getMoviesData() {
        return this.moviesData;
    }


    class MovieListData extends MutableLiveData<List<Movie>> {

        public MovieListData() {
            super();
            setValue(new LinkedList<Movie>());

        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
//                MovieListData.movieValue(dao.index());
            }).start();

        }

    }
}
