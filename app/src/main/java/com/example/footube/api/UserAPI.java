package com.example.footube.api;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.footube.BasicClasses.User;
import com.example.footube.MyApplication;
import com.example.footube.R;
import com.example.footube.dao.UserDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private MutableLiveData<Boolean> signUpResult;
    private MutableLiveData<Boolean> authenticateResult;
    private MutableLiveData<User> userData;
    Retrofit retrofit;
    private UserDao dao;
    WebServiceAPI webServiceAPI;
    public UserAPI(MutableLiveData<Boolean> signUpResult, MutableLiveData<Boolean> authenticateResult, MutableLiveData<User> userData, UserDao dao) {
        this.signUpResult = signUpResult;
        this.authenticateResult = authenticateResult;
        this.userData = userData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void addUser(User newuser) {
        Call<Void> call = webServiceAPI.createUser(newuser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to sign you up, try later :)"
                            , Toast.LENGTH_SHORT).show();
                    Log.d("response1:", response.toString());
                } else {
                    new Thread(() -> dao.insert(newuser)).start();
                    signUpResult.setValue(true);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server.", Toast.LENGTH_SHORT).show();
                Log.e("UserAPI", "Failed to connect to the server: ", t);
            }

        });
    }


    public void deleteUser(String username) {
        Call<Void> call = webServiceAPI.deleteUser(username);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to delete your user, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    //new Thread(() -> dao.delete(username)).start();
                    signUpResult.setValue(true);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server.", Toast.LENGTH_SHORT).show();
                Log.e("UserAPI", "Failed to connect to the server: ", t);
            }

        });
    }


    public void getUser(String username) {

        Call<User> call = webServiceAPI.getUser(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to get your User information"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.insert(response.body())).start();
                    userData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
