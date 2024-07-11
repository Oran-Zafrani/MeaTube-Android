package com.example.footube.BasicClasses;

import com.example.footube.BasicClasses.Movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private int id;
    private String username;
    private String displayName;
    private String password;
    private String image;
    private int subscribers;
    private List<Movie> likes;
    private List<Movie> unlikes;

    public User(String username, String displayName, String password, String image) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.image = image;
        this.likes = new ArrayList<>();
        this.unlikes = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean searchlike(Movie m){
        for (Movie temp : this.likes){
            if(Objects.equals(temp.getName(), m.getName()) && temp.GetUploadTime() == m.GetUploadTime()){
                return true;
            }
        }
        return false;
    }

    public boolean searchunlike(Movie m){
        for (Movie temp : this.unlikes){
            if(Objects.equals(temp.getName(), m.getName()) && temp.GetUploadTime() == m.GetUploadTime()){
                return true;
            }
        }
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Movie> getLikes() {
        return likes;
    }

    public List<Movie> getUnlikes() {
        return unlikes;
    }

    public void setLikes(List<Movie> likes) {
        this.likes = likes;
    }

    public void setUnlikes(List<Movie> unlikes) {
        this.unlikes = unlikes;
    }

    public void AddLike(Movie movie){
        this.likes.add(movie);
    }

    public void AddUnLike(Movie movie){
        this.unlikes.add(movie);
    }

    public void RemoveUnLike(Movie movie){
        this.unlikes.remove(movie);
    }

    public void RemoveLike(Movie movie){
        this.likes.remove(movie);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}