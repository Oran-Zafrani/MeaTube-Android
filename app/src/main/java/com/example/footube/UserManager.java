package com.example.footube;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserManager {
    private Map<String, User> userMap;

    public UserManager() {
        userMap = new HashMap<>();
        // Adding a sample user for demonstration
        User sampleUser = new User("bar", "Bar User", "123456", "default_profile.jpg");
        addUser(sampleUser);
    }

    // Add a user to the map
    public void addUser(String username, String displayName, String password, String image) {
        User newUser = new User(username, displayName, password, image);
        userMap.put(username, newUser);
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
}