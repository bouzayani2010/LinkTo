package com.project.linkto.bean;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbouzaiene on 11/05/2018.
 */

@IgnoreExtraProperties
public class Comment {

    private String content_text;
    private String timestamp;
    public String uid;

    public Comment() {
    }

    public Comment(String uid, String content_text, String timestamp) {
        this.uid = uid;
        this.content_text = content_text;
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("content_text", content_text);
        result.put("timestamp", timestamp);

        return result;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
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
}
