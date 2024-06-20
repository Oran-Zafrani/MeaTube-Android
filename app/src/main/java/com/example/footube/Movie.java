package com.example.footube;

import java.io.Serializable;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private String creator;
    private String name;
    private String description;
    private String category;
    private String movie;

    public Movie(String creator, String name, String description, String category, String movie) {
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.category = category;
        this.movie = movie;
    }

    public String getCreator() {
        return creator;
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

    @Override
    public String toString() {
        return "Movie{" +
                "creator='" + creator + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", movieUri='" + movie + '\'' +
                '}';
    }
}
