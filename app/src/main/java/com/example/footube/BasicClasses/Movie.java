package com.example.footube.BasicClasses;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String title;
    private String description;
    private String category;
    private String videoFile;
    private int likes;
    private int dislikes;
    private int views;
    private String previewImage;
    private List<Comment> commentsLink;
    private String channel;
    private int comments;
    private Date uploadTime;

    public Movie(String channel,String creator, String name, String description, String category, String movie, String uploadtime) {
        this.channel = channel;
        this.username = creator;
        this.title = name;
        this.description = description;
        this.category = category;
        this.videoFile = movie;
        this.likes = 0;
        this.dislikes = 0;
        this.views = 0;
        if (Objects.equals(uploadtime, "")){
            this.uploadTime = Calendar.getInstance().getTime();
        }else {
            this.uploadTime = convertStringToDate(uploadtime);
        }
        this.commentsLink = new ArrayList<Comment>();
        this.id = (int) (System.currentTimeMillis() / 1000L);
    }


    public void SetMovie(Movie m){
        this.title = m.getName();
        this.description = m.getDescription();
        this.category = m.getCategory();
        this.previewImage = m.getPreviewImage();
        this.videoFile = m.getMovieUri();
    }

    public String getChannel() {
        return channel;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public int getViews() {
        return views;
    }

    public void AddView(){
        this.views++;
    }

    public void AddComment(Comment comment){
        this.commentsLink.add(comment);
    }

    public List<Comment> GetComments(){
        return this.commentsLink;
    }

    public String getCreator() {
        return username;
    }

    public Date GetUploadTime() {
        return this.uploadTime;
    }

    public String GetImage() {
        return this.previewImage;
    }

    public int getLikes(){
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getUnlikes() {
        return dislikes;
    }

    public void setUnlikes(int unlikes) {
        this.dislikes = unlikes;
    }

    public void setCreator(String creator) {
        this.username = creator;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
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
        return videoFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieUri(String movie) {
        this.videoFile = movie;
    }

    public void setMovieImage(String image) {
        this.previewImage = image;
    }

    public String commentsstring(){
        String s="{";
        for (int i = 0; i < this.commentsLink.size(); i++) {
            s+="{"+this.commentsLink.get(i).getUsername() + "," + this.commentsLink.get(i).getComment() + "}";
        }
        s+="}";
        return s;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "creator='" + username + '\'' +
                "name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", movieUri='" + videoFile + '\'' +
                ", uploadtime='" + this.uploadTime + '\'' +
                ", likes='" + this.likes + '\'' +
                ", comments='" + commentsstring() + '\'' +
                '}';
    }

    public static Date convertStringToDate(String dateString) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            // Parse the date string to a Date object
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean[] getiscreator(String username){
        boolean[] iscreator = new boolean[commentsLink.size()+1];
        for (int i = 0; i < commentsLink.size(); i++) {
            iscreator[i] = Objects.equals(commentsLink.get(i).getUsername(), username);
        }
        return iscreator;
    }

    public String getRelativeTime() {
        long uploadTime = this.uploadTime.getTime();
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - uploadTime;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long days = TimeUnit.MILLISECONDS.toDays(duration);

        if (seconds < 60) {
            return seconds == 1 ? "1 second ago" : seconds + " seconds ago";
        } else if (minutes < 60) {
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else if (hours < 24) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else if (days < 30) {
            return days == 1 ? "1 day ago" : days + " days ago";
        } else if (days < 365) {
            long months = days / 30;
            return months == 1 ? "1 month ago" : months + " months ago";
        } else {
            long years = days / 365;
            return years == 1 ? "1 year ago" : years + " years ago";
        }
    }
}
