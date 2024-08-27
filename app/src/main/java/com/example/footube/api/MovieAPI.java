package com.example.footube.api;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;
import com.example.footube.MyApplication;
import com.example.footube.R;
import com.example.footube.Repository.TokenRepository;
import com.example.footube.Repository.UserRepository;
import com.example.footube.dao.MovieDao;
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
    private MovieDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private TokenRepository tokenRepository;
    private UserRepository usersRepository;

    public MovieAPI(MutableLiveData<List<Movie>> movieListData, MovieDao dao, MutableLiveData<List<Movie>> feedData) {
        this.movieListData = movieListData;
        this.dao = dao;

        // Initialize repositories
        this.tokenRepository = new TokenRepository();
        this.usersRepository = new UserRepository();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // Server's date format
                .create();


// Step 2: Create an OkHttpClient.Builder instance
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

// Step 3: Set the timeouts
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(60, TimeUnit.SECONDS);

// Step 4: Build the OkHttpClient instance
        OkHttpClient okHttpClient = builder.build();

// Step 5: Use the OkHttpClient instance when building the Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient) // Use the OkHttpClient with increased timeout
                .build();

        //OLD RETROFIT
//        retrofit = new Retrofit.Builder()
//                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public void addMovie(Movie newMovie) {
        User user = LoggedInUser.getInstance().getUser();
        Call<Void> call = webServiceAPI.createMovie(newMovie, tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 400){
                    Toast.makeText(MyApplication.context, "Unable to add the video"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to add the video, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        dao.insert(newMovie);
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void get() {
        Call<List<Movie>> call = webServiceAPI.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    // The request is successful
                    List<Movie> movies = response.body();
                    // Now you can use the list of movies
                    System.out.println(movies);
                } else {
                    // The request failed
                    System.out.println("Request failed. Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // The request failed completely
                System.out.println("Request failed with error: " + t.getMessage());
            }
        });


//        call.enqueue(new Callback<List<Movie>>() {
//            @Override
//            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
//
//                new Thread(() -> {
//                    dao.clear();
//                    dao.insert(response.body().toArray(new Movie[0]));
//                    movieListData.postValue(dao.index());
//                }).start();
//            }
//
//            @Override
//            public void onFailure(Call<List<Movie>> call, Throwable t) {
//                Toast.makeText(MyApplication.context, "Unable to connect to the server."
//                        , Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
