package com.project.linkto.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.project.linkto.R;
import com.project.linkto.adapter.ListPostAdapter;
import com.project.linkto.bean.Person;
import com.project.linkto.bean.Post;
import com.project.linkto.singleton.DataHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private TextView userName;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerView recyclerView;
    private ListPostAdapter mAdapter;
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
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Express yourself", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.gotoFeedPost();
                            }
                        }).show();
            }
        });


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

        mAdapter = new ListPostAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //mDatabase.child("users").
        mDatabase.child("posts").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    postList.clear();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.i("mamama", "::" + singleSnapshot.toString());
                        Post post = singleSnapshot.getValue(Post.class);
                        postList.add(post);
                        Log.i("mamama", "::" + post.toString());
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
        Log.i("person", personProfile.getCoverphoto());
        Log.i("person", personProfile.getProfilephoto());
        userName.setText(personProfile.getFirstname() + " " + personProfile.getLastname());
        final int radius = 10;
        final int margin = 10;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(personProfile.getCoverphoto()).fit().into(coverImg);
        Picasso.get().load(personProfile.getProfilephoto()).resize(1000, 1000)
                .transform(transformation)
               .transform(new CropSquareTransformation())
                .into(profileImg);
    }


}
