package com.example.footube;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserManager {
    private Map<String, String> userMap;

    public UserManager() {
        userMap = new HashMap<>();
        userMap.put("bar", "123");
    }

    // Add a user to the map
    public void addUser(User user) {
        userMap.put(user.getName(), user.getPassword());
    }

    // Remove a user from the map by username
    public void removeUser(String username) {
        userMap.remove(username);
    }

    // Get a password by username
    public String getPassword(String username) {
        return userMap.get(username);
    }

    // Check if a user exists
    public boolean userExists(String username) {
        return userMap.containsKey(username);
    }

    // Get all users
    public Map<String, String> getAllUsers() {
        return userMap;
    }

    @Override
    public String toString() {
        return "UserManager{" +
                "userMap=" + userMap +
                '}';
    }

    public boolean CorrectSignIn(String user, String password)
    {
        String pass =  getPassword(user);
        return Objects.equals(pass, password);
    }
}
