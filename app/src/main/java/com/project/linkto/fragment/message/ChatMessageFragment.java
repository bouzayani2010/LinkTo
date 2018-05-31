package com.project.linkto.fragment.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.linkto.R;
import com.project.linkto.adapter.viewsadapter.ListMessageAdapter;
import com.project.linkto.bean.ChatMessage;
import com.project.linkto.bean.GroupMessage;
import com.project.linkto.bean.Person;
import com.project.linkto.bean.SampleSearchModel;
import com.project.linkto.bean.Userbd;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.linkto.BaseActivity.mDatabase;

/**
 * Created by bbouzaiene on 16/05/2018.
 */

public class ChatMessageFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private EditText ed_content_text;
    private Userbd userbd;
    private ImageView bt_submit;
    private Person mPerson;
    private String mUserId;
    private GroupMessage groupMessage;
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private ListMessageAdapter mAdapter;
    private String key;
    private TextView tv_name;
    private FloatingSearchView floating_search_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        userbd = DataHelper.getInstance().getmUserbd();
        ed_content_text = (EditText) view.findViewById(R.id.ed_content_text);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        bt_submit = (ImageView) view.findViewById(R.id.bt_submit);
        floating_search_view = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        drawViews();

        return view;

    }

    private void drawViews() {
        mAdapter = new ListMessageAdapter(mActivity, messageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        floating_search_view.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchUser(newQuery);
            }
        });

        if (groupMessage != null) {
            key = groupMessage.getKey();
            floating_search_view.setVisibility(View.GONE);
        } else {
            tv_name.setVisibility(View.VISIBLE);
            floating_search_view.setVisibility(View.VISIBLE);
        }

        if (key != null) {

            mDatabase.child("users").child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Person personProfile = dataSnapshot.getValue(Person.class);

                    tv_name.setText(personProfile.getFirstname() + " " + personProfile.getLastname());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mDatabase.child("messages").child(key).child("content").getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        messageList.clear();
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Log.i("mamama", "::" + singleSnapshot.toString());
                            ChatMessage chatMessage = singleSnapshot.getValue(ChatMessage.class);
                            //chatMessage.setKey(singleSnapshot.getKey());
                            //
                            messageList.add(chatMessage);
                            Log.i("mamama", "::" + chatMessage.toString());

                        }
                    } catch (Exception e) {
                        Log.i("mamama", "::" + e.getMessage());
                        e.printStackTrace();
                    }
                    //  Collections.sort(postList);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content_text = ed_content_text.getText().toString();
                if (!Utils.isEmptyString(content_text)) {
                    writeNewMessage(content_text);
                    ed_content_text.setText("");
                } else {
                    new MaterialDialog.Builder(mActivity)
                            .title(R.string.message)
                            .content(R.string.emptymessage)
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
    }

    private void searchUser(String queryText) {



        Query queryRef = mDatabase.child("users").getRef().orderByChild("firstname")
                .startAt(queryText)
                .endAt(queryText + "\uf8ff");
        //.on("value");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("usersusers", "1::" + dataSnapshot.toString());
                List<SampleSearchModel> newSuggestions = new ArrayList<SampleSearchModel>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.i("mamama1", "::" + singleSnapshot.toString());
                    Person person = singleSnapshot.getValue(Person.class);

                    Log.i("usersusers", "::" + person.getFirstname());
                    SampleSearchModel sampleSearchModel = new SampleSearchModel(person.getFirstname() + " " + person.getLastname());
                    newSuggestions.add(sampleSearchModel);

                    Log.i("usersusers", "::" + newSuggestions);


                }

                floating_search_view.swapSuggestions(newSuggestions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeNewMessage(String content_text) {
        //  String key = mDatabase.child("messages").push().getKey();
        if (groupMessage == null) {
            groupMessage = new GroupMessage();

            key = mDatabase.child("messages").push().getKey();
            groupMessage.setKey(key);

            List<String> listUserId = new ArrayList<String>();
            listUserId.add(mUserId);
            listUserId.add(userbd.getUid());
            groupMessage.setListUserId(listUserId);
            drawViews();

            Map<String, Object> groupsUpdates = new HashMap<>();
            String groupkey1 = mDatabase.child("messages").child(key).child("groups").push().getKey();
            String groupkey2 = mDatabase.child("messages").child(key).child("groups").push().getKey();
            groupsUpdates.put(groupkey1, userbd.getUid());
            groupsUpdates.put(groupkey2, mUserId);
            mDatabase.child("messages").child(key).child("groups").updateChildren(groupsUpdates);


            Map<String, Object> idsdUpdates = new HashMap<>();
            String keyinUser = mDatabase.child("users").child(userbd.getUid()).child("messageid").push().getKey();
            idsdUpdates.put(keyinUser, key);
            mDatabase.child("users").child(userbd.getUid()).child("messageid").updateChildren(idsdUpdates);

            Map<String, Object> idsOthersUpdates = new HashMap<>();
            String keyinotherUser = mDatabase.child("users").child(mUserId).child("messageid").push().getKey();
            idsOthersUpdates.put(keyinotherUser, key);
            mDatabase.child("users").child(mUserId).child("messageid").updateChildren(idsdUpdates);

        }
        try {
            key = groupMessage.getKey();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatMessage chatmessage = new ChatMessage(content_text, userbd.getUid());
        Map<String, Object> messagesValues = chatmessage.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        String key1 = mDatabase.child("messages").child(key).child("content").push().getKey();
        childUpdates.put(key1, messagesValues);
        mDatabase.child("messages").child(key).child("content").updateChildren(childUpdates);


    }

    public void setmUser(Person mPerson) {
        this.mPerson = mPerson;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setGroupMessage(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }
}
