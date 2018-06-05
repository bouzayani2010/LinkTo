package com.project.linkto.bean;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbouzaiene on 04/05/2018.
 */
@IgnoreExtraProperties
public class Post implements Comparable<Post> {
    private String timestamp;
    public String uid;
    public String author;
    public String title;
    public String body;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, Like> likes = new HashMap<>();
    private String key;
    private int commentCount;
    private int shareCount;
    private String originPostId;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String body, String timestamp) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("commentCount", commentCount);
        result.put("shareCount", shareCount);
        result.put("stars", stars);
        result.put("timestamp", timestamp);
        result.put("likes", likes);
        result.put("originPostId", originPostId);

        return result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Map<String, Like> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Like> likes) {
        this.likes = likes;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    @Override
    public int compareTo(@NonNull Post other) {
        java.sql.Timestamp ts1 = java.sql.Timestamp.valueOf(this.getTimestamp());
        java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(other.getTimestamp());

        Log.i("yeardaymonth",ts1.getTime()+" "+ts2.getTime());
        if(ts2.getTime()>=ts1.getTime())
        return 1;
        else return -1;
    }

    public void setOriginPostId(String originPostId) {
        this.originPostId = originPostId;
    }

    public String getOriginPostId() {
        return originPostId;
    }
}
