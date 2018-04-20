package com.project.linkto.Fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private TextView userName;
    private List<Person> movieList = new ArrayList<Person>();
    private RecyclerView recyclerView;
    private ListPersonAdapter mAdapter;
    private DatabaseReference mDatabase;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabase = mActivity.database.getReference();

        userName = (TextView) view.findViewById(R.id.nameuser);
        if (DataHelper.getInstance().isConnected()) {
            userName.setText(DataHelper.getInstance().getmUser().getEmail());
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new ListPersonAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mDatabase.child("users").getRef().orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                try {
                    Person person = dataSnapshot.getValue(Person.class);
                    Log.i("mamama", "::" + person.toString());
                } catch (Exception e) {  Log.i("mamama", "::" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("users").child("users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map map = (Map) dataSnapshot;
                    Log.i("mamama", "::" + map.toString());
                } catch (Exception e) {
                    Log.i("mamama", "::" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //preparePersonData();
        return view;

    }


}
