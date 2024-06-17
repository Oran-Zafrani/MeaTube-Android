package com.example.footube;

public class Comment {
    private String username;
    private String comment;

    // Constructor
    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for comment
    public String getComment() {
        return comment;
    }

    // Setter for comment
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Optional: Override toString method for easy printing
    @Override
    public String toString() {
        return "Comment{" +
                "username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
