package com.project.linkto.singleton;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.project.linkto.bean.GroupMessage;
import com.project.linkto.bean.Post;
import com.project.linkto.bean.Userbd;
import com.project.linkto.database.DatabaseManager;
import com.project.linkto.database.Userrepo;

import java.util.List;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class DataHelper {
    private static boolean connected;
    private static DataHelper instance;
    private FirebaseUser mUser;
    private Userrepo uRepo;
    private Userbd mUserbd;
    private List<GroupMessage> mGroupMessageList;
    private List<Post> mypostList;

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


    public Userrepo getuRepo() {
        return uRepo;
    }

    public void setuRepo(Userrepo uRepo) {
        this.uRepo = uRepo;
    }

    public void setmUserbd(Userbd mUserbd) {
        this.mUserbd = mUserbd;
    }

    public Userbd getmUserbd() {
        return mUserbd;
    }

    public void initDB(Context context) {
        DatabaseManager.init(context);
        this.setuRepo(new Userrepo(context));
    }

    public void setmGroupMessageList(List<GroupMessage> mGroupMessageList) {
        this.mGroupMessageList = mGroupMessageList;
    }

    public List<GroupMessage> getmGroupMessageList() {
        return mGroupMessageList;
    }

    public void setMypostList(List<Post> mypostList) {
        this.mypostList = mypostList;
    }


    public List<Post> getMypostList() {
        return mypostList;
    }
}
