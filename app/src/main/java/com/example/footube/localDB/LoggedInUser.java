package com.example.footube.localDB;

import com.example.footube.BasicClasses.User;

public class LoggedInUser {

    private static LoggedInUser instance;
    private User user;

    private LoggedInUser() {}

    public static LoggedInUser getInstance() {
        if (instance == null) {
            instance = new LoggedInUser();
        }
        return instance;
    }

    public static boolean thereIsUser(){
        if (instance == null)
            return false;
        return true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void deleteLoggedInUser() {
        user = null;        // Clear the reference to the logged-in user
        instance = null;    // Reset the singleton instance
    }
}