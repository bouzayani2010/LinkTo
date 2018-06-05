package com.project.linkto.fragment.feeds;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.project.linkto.R;
import com.project.linkto.adapter.viewsadapter.ListCommentAdapter;
import com.project.linkto.adapter.viewsadapter.ListPostAdapter;
import com.project.linkto.bean.Comment;
import com.project.linkto.bean.Post;
import com.project.linkto.bean.Userbd;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bbouzaiene on 11/05/2018.
 */

public class CommentFragment extends BaseFragment {

    private Post post;
    private EditText ed_content_text;
    private ImageView bt_submit;
    private DatabaseReference mDatabase;
    private Userbd userbd;
    private RecyclerView recyclerView;
    private List<Comment> commentList = new ArrayList<Comment>();
    private ListCommentAdapter mAdapter;
    private RecyclerView recyclerViewPost;
    private ListPostAdapter mPostAdapter;

    @SuppressLint("ValidFragment")
    public CommentFragment(Post post) {
        super();
        this.post=post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        userbd = DataHelper.getInstance().getmUserbd();
        mDatabase = mActivity.database.getReference();
        ed_content_text = (EditText) view.findViewById(R.id.ed_content_text);
        bt_submit = (ImageView) view.findViewById(R.id.bt_submit);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerViewPost = (RecyclerView) view.findViewById(R.id.recycler_view_post);

        drawViews();
        return view;
    }

    private void drawViews() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content_text = ed_content_text.getText().toString();
                if (!Utils.isEmptyString(content_text)) {
                    writeNewComment(post, content_text);
                    ed_content_text.setText("");
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


        mAdapter = new ListCommentAdapter(commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        List<Post> postList = new ArrayList<Post>();
        postList.add(post);


        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mActivity);
        mPostAdapter = new ListPostAdapter(postList);
        recyclerViewPost.setLayoutManager(mLayoutManager1);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPost.setAdapter(mPostAdapter);

        Log.i("mamama", "::" + post.toString());
        mDatabase.child("posts").child(post.getKey()).child("comments").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    commentList.clear();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.i("mamama", "::" + singleSnapshot.toString());
                        Comment commentFeed = singleSnapshot.getValue(Comment.class);
                        //    post.setKey(singleSnapshot.getKey());
                        //
                        commentList.add(commentFeed);
                        Log.i("mamama", "::" + commentFeed.toString());

                    }
                } catch (Exception e) {
                    Log.i("mamama", "::" + e.getMessage());
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeNewComment(Post post, String content_text) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String key = mDatabase.child("posts").child(post.getKey()).child("comments").push().getKey();
        Comment commentFeed = new Comment(userbd.getUid(), content_text, timestamp.toString());
        Map<String, Object> postValues = commentFeed.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        // childUpdates.put("/comments/" + key, postValues);
        childUpdates.put("" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.child("posts").child(post.getKey()).child("comments").updateChildren(childUpdates);
        mDatabase.child("posts").child(post.getKey()).child("commentCount").setValue(post.getCommentCount() + 1);
    }

    public Post getPost() {
        return post;
    }
}
