package com.project.linkto.bean;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbouzaiene on 04/05/2018.
 */
@IgnoreExtraProperties
public class Like {


    private String timestamp;
    private String keypost;
    private String uid;

    public Like() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Like(String uid, String keypost, String timestamp) {
        this.uid = uid;
        this.keypost = keypost;
        this.timestamp = timestamp;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("keypost", keypost);
        result.put("timestamp", timestamp);

        return result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKeypost() {
        return keypost;
    }

    public void setKeypost(String keypost) {
        this.keypost = keypost;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
