package com.project.linkto.fragment.message;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.project.linkto.R;
import com.project.linkto.adapter.viewsadapter.ListTitleMessageAdapter;
import com.project.linkto.bean.GroupMessage;
import com.project.linkto.bean.Userbd;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.singleton.DataHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.project.linkto.BaseActivity.mDatabase;

/**
 * Created by bbouzaiene on 16/05/2018.
 */

public class ChatListMessageFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private EditText ed_content_text;
    private Userbd userbd;
    private List<GroupMessage> groupMessageList;
    private ListTitleMessageAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_message, container, false);
        userbd = DataHelper.getInstance().getmUserbd();
        ed_content_text = (EditText) view.findViewById(R.id.ed_content_text);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        groupMessageList = new ArrayList<GroupMessage>();
        drawViews();

        return view;

    }

    private void drawViews() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessageFragment chatMessageFragment = new ChatMessageFragment();
                mActivity.gotoChatMessage(chatMessageFragment);
            }
        });
        mDatabase.child("messages").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupMessageList.clear();;
                GroupMessage groupMessage = new GroupMessage();
                List<String> listUserId = new ArrayList<String>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.i("groups", "::" + singleSnapshot.toString());
                    Map<String, Map> map = (Map<String, Map>) singleSnapshot.getValue();
                    Map groups = map.get("groups");
                    Log.i("groups", "::" + groups.toString());
                    listUserId.addAll(groups.values());
                    listUserId.remove(userbd.getUid());
                    groupMessage.setListUserId(listUserId);
                }
                groupMessage.setListUserId(listUserId);
                groupMessageList.add(groupMessage);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new ListTitleMessageAdapter(mActivity,groupMessageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


}
