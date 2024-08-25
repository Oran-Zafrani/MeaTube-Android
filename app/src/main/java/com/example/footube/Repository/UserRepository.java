package com.example.footube.Repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.footube.BasicClasses.User;
import com.example.footube.MyApplication;
import com.example.footube.api.UserAPI;
import com.example.footube.dao.UserDao;
import com.example.footube.localDB.AppDB;

public class UserRepository extends Application {


    private final UserDao dao;
    private final UserAPI api;
    private MutableLiveData<Boolean> signUpResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> authenticateResult = new MutableLiveData<>();
    private MutableLiveData<User> userData = new MutableLiveData<>();

    public UserRepository(){
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "userDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        this.dao = db.userDao();
        api = new UserAPI(signUpResult, authenticateResult, userData, dao);
    }

    public void addUser(User newuser) {
        api.addUser(newuser);
    }
}
