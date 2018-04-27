package com.project.linkto.Fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.project.linkto.R;
import com.project.linkto.adapter.ListPersonAdapter;
import com.project.linkto.bean.Person;
import com.project.linkto.singleton.DataHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private TextView userName;
    private List<Person> personList = new ArrayList<Person>();
    private RecyclerView recyclerView;
    private ListPersonAdapter mAdapter;
    private DatabaseReference mDatabase;
    private ImageView coverImg;
    private ImageView profileImg;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabase = mActivity.database.getReference();

        userName = (TextView) view.findViewById(R.id.nameuser);
        coverImg = (ImageView) view.findViewById(R.id.coverimg);
        profileImg = (ImageView) view.findViewById(R.id.profileimg);


        if (DataHelper.getInstance().isConnected()) {

            try {
                String uidProfile = DataHelper.getInstance().getmUserbd().getUid();
                mDatabase.child("users").child(uidProfile).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Person personProfile = dataSnapshot.getValue(Person.class);
                        drawPersonViews(personProfile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
              //  userName.setText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new ListPersonAdapter(personList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //mDatabase.child("users").
        mDatabase.child("users").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    personList.clear();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.i("mamama", "::" + singleSnapshot.toString());
                        Person person = singleSnapshot.getValue(Person.class);
                        personList.add(person);
                        Log.i("mamama", "::" + person.toString());
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
        //preparePersonData();
        return view;

    }

    private void drawPersonViews(Person personProfile) {
        Log.i("person",personProfile.getCoverphoto());
        Log.i("person",personProfile.getProfilephoto());
        userName.setText(personProfile.getFirstname()+" "+ personProfile.getLastname());
        Picasso.get().load(personProfile.getCoverphoto()).resize(2000,1000).centerCrop().into(coverImg);
        Picasso.get().load(personProfile.getProfilephoto()).into(profileImg);
    }


}
