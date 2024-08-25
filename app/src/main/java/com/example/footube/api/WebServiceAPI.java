package com.example.footube.api;

import com.example.footube.BasicClasses.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface WebServiceAPI {
    @POST("users")
    Call<Void> createUser(@Body User user);
}
