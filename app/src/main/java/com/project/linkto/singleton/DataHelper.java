package com.project.linkto.singleton;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class DataHelper {
    private static boolean connected;
    private static DataHelper instance;
    private FirebaseUser mUser;

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean connected) {
        DataHelper.connected = connected;
    }

    public static DataHelper getInstance() {
        if (instance == null) {
            instance = new DataHelper();
        }
        return instance;
    }


    public void setmUser(FirebaseUser mUser) {
        this.mUser = mUser;
    }


    public FirebaseUser getmUser() {
        return mUser;
    }
}
