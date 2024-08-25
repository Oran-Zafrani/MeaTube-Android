package com.example.footube.managers;

import android.content.Context;
import android.util.Log;

import com.example.footube.BasicClasses.User;
import com.example.footube.ViewModel.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserManager {
    private static UserManager instance;
    private Map<String, User> userMap;
    private UserViewModel viewModel;

    // Private constructor to prevent instantiation
    private UserManager() {
        userMap = new HashMap<>();
        viewModel = new UserViewModel();
        // Adding a sample user for demonstration
        User sampleUser = new User("ba", "Bar User", "123", "default_profile.jpg");
        User sampleUser1 = new User("Guest", "Guest", "123", "default_profile.jpg"); //for no option to create Guest username
        addUser(sampleUser);
        addUser(sampleUser1);
    }

    // Method to get the single instance of UserManager
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Add a user to the map
    public void addUser(String username, String displayName, String password, String image) {
        User newUser = new User(username, displayName, password, image);
        //userMap.put(username, newUser);
        viewModel.addUser(newUser);
    }

    // Add a user to the map
    public void addUser(User user) {
        userMap.put(user.getUsername(), user);
    }

    // Remove a user from the map by username
    public void removeUser(String username) {
        userMap.remove(username);
    }

    // Get a user by username
    public User getUser(String username) {
        return userMap.get(username);
    }

    // Check if a user exists
    public boolean userExists(String username) {
        return userMap.containsKey(username);
    }

    // Get all users
    public Map<String, User> getAllUsers() {
        return userMap;
    }

    // Method to verify user credentials
    public boolean correctSignIn(String username, String password) {
        User user = getUser(username);
        return user != null && Objects.equals(user.getPassword(), password);
    }

    @Override
    public String toString() {
        return "UserManager{" +
                "userMap=" + userMap +
                '}';
    }

    // Load users from JSON file
    public void loadUsersFromJSON(Context context) {
        try {
            InputStream is = context.getAssets().open("Users.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Type userListType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(json, userListType);

            for (User user : users) {
                if (user.getLikes() == null) {
                    user.setLikes(new ArrayList<>());
                }
                if (user.getDislike() == null) {
                    user.setDislike(new ArrayList<>());
                }
                int resourceId = context.getResources().getIdentifier(user.getImage(), "raw", context.getPackageName());
                if (resourceId != 0) {
                    String readuser = MoviesManager.readTextFileFromRaw(context, resourceId);
                    user.setImage(readuser);
                }
                addUser(user);
            }
        } catch (IOException ex) {
            Log.e("UserManager", "Error reading Users.json", ex);
        }
    }
    private String readTextFileFromRaw(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }
}