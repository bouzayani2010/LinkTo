package com.project.linkto.bean;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbouzaiene on 27/04/2018.
 */

@DatabaseTable
@IgnoreExtraProperties
public class Userbd {

    @DatabaseField(generatedId = true)
    private static final int id = 1;
    @DatabaseField
    private String email;
    @DatabaseField
    private String displayName;
    @DatabaseField
    private String uid;
    @DatabaseField
    private String photoUrl;
    private String firstname;
    private Object lastname;

    public Userbd(FirebaseUser user) {
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        try {
            this.photoUrl = user.getPhotoUrl().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.uid = user.getUid();
    }


    @Exclude
    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("firstname", firstname);
        result.put("lastname", lastname);

        return result;
    }

    public Userbd() {
    }

    public static int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
