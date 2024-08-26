package com.example.footube.Repository;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;
import com.example.footube.MyApplication;
import com.example.footube.R;
import com.example.footube.api.WebServiceAPI;
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

    public MovieAPI(MutableLiveData<List<Movie>> postListData, MovieDao dao, MutableLiveData<List<Movie>> wallPostData) {
        this.movieListData = movieListData;
        this.dao = dao;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // Server's date format
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
//        tokenRepository = new TokenRepository();
//        usersRepository = new UsersRepository();
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
}
