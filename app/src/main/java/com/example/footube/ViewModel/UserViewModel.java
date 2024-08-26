package com.example.footube.ViewModel;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.footube.BasicClasses.User;
import com.example.footube.Repository.UserRepository;

public class UserViewModel extends ViewModel {
    private LiveData<Boolean> signUpResult;
    private LiveData<Boolean> authenticateResult;
    private LiveData<User> userLiveData;
    private UserRepository repository;

    public UserViewModel(){
        repository = new UserRepository();
        //signUpResult = repository.getSignUpResult();
        authenticateResult = repository.getAuthenticateResult();
        userLiveData = repository.getUserData();
    }

    public void addUser(User newuser) {
        repository.addUser(newuser);
    }

    public void deleteUser(String username) {
        repository.deleteUser(username);
    }

    public void getUser(String username) {
         repository.getUser(username);
    }

    public LiveData<User> getUserLiveData(){
        return userLiveData;
    }

    public LiveData<Boolean> getAuthenticateResult() {
        return authenticateResult;
    }

    public void authenticate(String username, String password) {
        repository.authenticate(username, password);
    }

    public LiveData<User> getLoggedInUser(String username) {
        repository.getUser(username);
        return userLiveData;
    }

//    public LiveData<User> getUser(String username) {
//        userLiveData = repository.getUser(username);
//        return userLiveData;
//    }
}
