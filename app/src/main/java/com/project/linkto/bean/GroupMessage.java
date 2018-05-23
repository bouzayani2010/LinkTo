package com.project.linkto.bean;

import java.util.List;

/**
 * Created by bbouzaiene on 21/05/2018.
 */

public class GroupMessage {
    private List<String> listUserId;
    private String key;

    public void setListUserId(List<String> listUserId) {
        this.listUserId = listUserId;
    }

    public List<String> getListUserId() {
        return listUserId;
    }

    public void setKey(String key) {
        this.key = key;

    }

    public String getKey() {
        return key;
    }
}
