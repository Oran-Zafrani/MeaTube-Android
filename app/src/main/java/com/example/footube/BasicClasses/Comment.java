package com.example.footube.BasicClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
@Entity
public class Comment implements Serializable {
    private String _id;
    private String videoId;
    @PrimaryKey (autoGenerate = true)
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(int likesNum) {
        this.likesNum = likesNum;
    }

    public int getDislikesNum() {
        return dislikesNum;
    }

    public void setDislikesNum(int dislikesNum) {
        this.dislikesNum = dislikesNum;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getCommentId() {
        return commentId;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
