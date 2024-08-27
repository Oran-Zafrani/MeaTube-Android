package com.example.footube.api;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.Token;
import com.example.footube.BasicClasses.User;
import com.example.footube.requests.LoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface WebServiceAPI {
    //users
    @POST("users")
    Call<Void> createUser(@Body User user);

    @DELETE("users/{username}")
    Call<Void> deleteUser(@Path("username") String username);

    @GET("users/username/{username}")
    Call<User> getUser(@Path("username") String username);

    @POST("login")
    Call<Token> authenticateUser(@Body LoginRequest loginRequest);

    @PUT("users/{username}")
    Call<User> updateUser(@Path("username") String username, @Header("Authorization") String token, @Body User updatedUser);

    //Movies
    @POST("videos")
    Call<Void> createMovie(@Body Movie movie, @Header("Authorization")String token);
    @GET("videos")
    Call<List<Movie>> getMovies();
}