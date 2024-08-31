package com.example.footube.api;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import okhttp3.OkHttpClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;
import com.example.footube.MyApplication;
import com.example.footube.R;
import com.example.footube.Repository.TokenRepository;
import com.example.footube.dao.MovieDao;
import com.example.footube.listeners.MovieAdapter;
import com.example.footube.localDB.LoggedInUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieAPI {
    private MutableLiveData<List<Movie>> movieListData;
    private MutableLiveData<Movie> movieData;
    private MovieDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private TokenRepository tokenRepository;

    public MovieAPI(MutableLiveData<List<Movie>> movieListData, MovieDao dao, MutableLiveData<Movie> movieData) {
        this.movieListData = movieListData;
        this.tokenRepository = new TokenRepository();
        this.dao = dao;
//        this.tokenRepository = new TokenRepository();
        this.movieData = movieData;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        Call<List<Movie>> call = webServiceAPI.getMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {

                new Thread(() -> {
                    dao.clear();
                    dao.insert(response.body().toArray(new Movie[0]));
                    Log.d("MovieAPI", "Movies in RESPONSE: " + response.body().size());
                    List<Movie> moviesFromDb = dao.index();
                    Log.d("MovieAPI", "Movies in DB: " + moviesFromDb.size());
                    movieListData.postValue(moviesFromDb);
                }).start();
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addMovie(Movie newMovie) {
        Call<Void> call = webServiceAPI.createMovie(newMovie, tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to add the video, try later :)", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert the movie directly without using Collections.singletonList
                    new Thread(() -> dao.insert(newMovie)).start();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getMovies() {
        Call<List<Movie>> call = webServiceAPI.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        dao.clear();
                        dao.insert(response.body().toArray(new Movie[0]));
                        List<Movie> moviesFromDb = dao.index();
                        Log.d("MovieAPI", "Movies in DB: " + moviesFromDb.size());
                        movieListData.postValue(moviesFromDb);
//                        Log.d("`res18760`", response.body().toString());
                    }).start();
                } else {
                    Toast.makeText(MyApplication.context, "Unable to retrieve movies."
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMovie(String id) {
        Call<Movie> call = webServiceAPI.getMovie(id, tokenRepository.get());
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to get movie information"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.insert(response.body())).start();
                    movieData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
