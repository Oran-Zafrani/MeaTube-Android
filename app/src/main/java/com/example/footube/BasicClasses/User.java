package com.example.footube.BasicClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String displayName;
    private String password;
    private String image;

    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    private int subscribers;
    private List<Movie> likes;
    private List<Movie> dislike;

    public User(String username, String displayName, String password, String image) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.image = image;
        this.likes = new ArrayList<>();
        this.dislike = new ArrayList<>();
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
        for (Movie temp : this.dislike){
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

    public List<Movie> getDislike() {
        return dislike;
    }

    public void setLikes(List<Movie> likes) {
        this.likes = likes;
    }

    public void setDislike(List<Movie> dislike) {
        this.dislike = dislike;
    }

    public void AddLike(Movie movie){
        this.likes.add(movie);
    }

    public void AddUnLike(Movie movie){
        this.dislike.add(movie);
    }

    public void RemoveUnLike(Movie movie){
        this.dislike.remove(movie);
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