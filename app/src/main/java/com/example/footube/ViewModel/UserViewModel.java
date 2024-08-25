package com.example.footube.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.footube.BasicClasses.User;
import com.example.footube.Repository.UserRepository;

public class UserViewModel extends ViewModel {

    private UserRepository repository;

    public UserViewModel(){
        repository = new UserRepository();
    }

    public void addUser(User newuser) {
        repository.addUser(newuser);
    }
}
