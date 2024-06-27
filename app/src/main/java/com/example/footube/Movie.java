package com.example.footube;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private String creator;
    private String name;
    private String description;
    private String category;
    private String movie;
    private Date uploadtime;
    private int likes;
    private int unlikes;
    private String image;
    private List<Comment> comments;

    public Movie(String creator, String name, String description, String category, String movie) {
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.category = category;
        this.movie = movie;
        this.likes = 0;
        this.uploadtime = Calendar.getInstance().getTime();
        this.comments = new ArrayList<Comment>();
    }

    public void AddComment(Comment comment){
        this.comments.add(comment);
    }

    public List<Comment> GetComments(){
        return this.comments;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setUnlikes(int unlikes) {
        this.unlikes = unlikes;
    }

    public int getUnlikes() {
        return unlikes;
    }

    public int getLikes() {
        return likes;
    }

    public String getCreator() {
        return creator;
    }

    public Date GetUploadTime() {
        return this.uploadtime;
    }

    public String GetImage() {
        return this.image;
    }

    public void SetImage(String image){
        this.image = image;
    }

    public int GetLikes(){
        return this.likes;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMovieUri() {
        return movie;
    }

    public void setMovieUri(String movie) {
        this.movie = movie;
    }

    public void setMovieImage(String image) {
        this.image = image;
    }

    public String commentsstring(){
        String s="{";
        for (int i = 0; i < this.comments.size(); i++) {
            s+="{"+this.comments.get(i).getUsername() + "," + this.comments.get(i).getComment() + "}";
        }
        s+="}";
        return s;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "creator='" + creator + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", movieUri='" + movie + '\'' +
                ", uploadtime='" + this.uploadtime + '\'' +
                ", likes='" + this.likes + '\'' +
                ", comments='" + commentsstring() + '\'' +
                '}';
    }
}
