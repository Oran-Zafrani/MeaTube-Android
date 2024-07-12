package com.example.footube.BasicClasses;

import java.util.Date;

public class Comment {
    private int commentId;
    private String userName;
    private String displayName;
    private String commentText;
    private String userImage;
    private Date timestamp;
    private int likesNum;
    private int dislikesNum;

    // Constructor
    public Comment(String displayName,String username, String comment, int likesNum, int dislikesNum, String userImage) {
        this.userName = username;
        this.commentText = comment;
        this.dislikesNum = dislikesNum;
        this.likesNum = likesNum;
        this.displayName = displayName;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Getter for username
    public String getUsername() {
        return userName;
    }

    // Setter for username
    public void setUsername(String username) {
        this.userName = username;
    }

    // Getter for comment
    public String getComment() {
        return commentText;
    }

    // Setter for comment
    public void setComment(String comment) {
        this.commentText = comment;
    }

    // Optional: Override toString method for easy printing
    @Override
    public String toString() {
        return "Comment{" +
                "username='" + userName + '\'' +
                ", comment='" + commentText + '\'' +
                '}';
    }
}
