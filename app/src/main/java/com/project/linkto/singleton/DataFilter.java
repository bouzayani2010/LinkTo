package com.project.linkto.singleton;

import com.project.linkto.bean.GroupMessage;
import com.project.linkto.bean.Like;
import com.project.linkto.bean.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class DataFilter {
    private static DataFilter instance;


    public static DataFilter getInstance() {
        if (instance == null) {
            instance = new DataFilter();
        }
        return instance;
    }


    public String liked(Post post, String userId) {
        try {
            Map<String, Like> mapLikes = post.getLikes();

            List<String> listOfLikeskey = new ArrayList<String>(mapLikes.keySet());
            for (String lkey : listOfLikeskey) {
                Like lk = mapLikes.get(lkey);
                if (lk.getUid().equals(userId)) {
                    return lkey;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public GroupMessage getGroupMessage(String mUserId) {
        List<GroupMessage> groupMessageList = DataHelper.getInstance().getmGroupMessageList();
        if (groupMessageList != null && groupMessageList.size() > 0) {
            for (GroupMessage groupMessage : groupMessageList) {
                List<String> listUserId = groupMessage.getListUserId();
                if (listUserId.contains(mUserId)) {
                    return groupMessage;
                }
            }
        }
        return null;
    }
}
