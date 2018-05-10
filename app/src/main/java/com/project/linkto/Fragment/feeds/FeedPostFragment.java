package com.project.linkto.Fragment.feeds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.project.linkto.Fragment.BaseFragment;
import com.project.linkto.R;
import com.project.linkto.bean.Post;
import com.project.linkto.bean.Userbd;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbouzaiene on 04/05/2018.
 */

public class FeedPostFragment extends BaseFragment {

    private Button bt_submit;
    private EditText ed_content_text;
    private DatabaseReference mDatabase;
    private Userbd userbd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedpost, container, false);
        mDatabase = mActivity.database.getReference();
        userbd=DataHelper.getInstance().getmUserbd();

        ed_content_text = (EditText) view.findViewById(R.id.ed_content_text);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content_text = ed_content_text.getText().toString();
                if (!Utils.isEmptyString(content_text)) {
                    writeNewPost(userbd.getUid(),userbd.getDisplayName(),content_text.substring(0,15),content_text);
                } else {
                    new MaterialDialog.Builder(mActivity)
                            .title(R.string.publicationempty)
                            .content(R.string.publicationemptymessage)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        return view;
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body,timestamp.toString(),0);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
