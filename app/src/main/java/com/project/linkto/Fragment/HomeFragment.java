package com.project.linkto.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.project.linkto.R;
import com.project.linkto.adapter.ListPostAdapter;
import com.project.linkto.bean.Person;
import com.project.linkto.bean.Post;
import com.project.linkto.singleton.DataHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import static com.project.linkto.BaseActivity.mDatabase;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private TextView userName;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerView recyclerView;
    public ListPostAdapter mAdapter;
    private ImageView coverImg;
    private ImageView profileImg;
    private ImageView logoutImg;
    private FloatingActionButton fab;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userName = (TextView) view.findViewById(R.id.nameuser);
        coverImg = (ImageView) view.findViewById(R.id.coverimg);
        logoutImg = (ImageView) view.findViewById(R.id.logout);
        profileImg = (ImageView) view.findViewById(R.id.profileimg);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        drawViews();


        //preparePersonData();
        return view;

    }

    private void drawViews() {
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
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.logout)
                        .content(R.string.logoutmessage)
                   .show();
            }
        });
        logoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.logout)
                        .content(R.string.logoutmessage)
                        .positiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                mActivity.removeSavedUser();
                            }
                        })
                        .negativeText(R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();


            }
        });





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
                        post.setKey(singleSnapshot.getKey());
                        //
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
    }

    private void drawPersonViews(Person personProfile) {
        try {
            Log.i("person", personProfile.getCoverphoto());
            Log.i("person", personProfile.getProfilephoto());
            userName.setText(personProfile.getFirstname() + " " + personProfile.getLastname());
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(getResources().getColor(R.color.colorAccent))
                    .borderWidthDp(3)
                    .cornerRadiusDp(20)
                    .oval(false)
                    .build();
            Picasso.get().load(personProfile.getCoverphoto()).fit().into(coverImg);
            Picasso.get().load(personProfile.getProfilephoto()).resize(1000, 1000)
                    .centerCrop().transform(transformation)
                    .into(profileImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataHelper.getInstance().isConnected()) {
            logoutImg.setVisibility(View.VISIBLE);
            try {
                String uidProfile = DataHelper.getInstance().getmUserbd().getUid();
                mDatabase.child("users").child(uidProfile).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Person personProfile = dataSnapshot.getValue(Person.class);
                        if (personProfile != null)
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
        } else {
            logoutImg.setVisibility(View.INVISIBLE);
        }
    }
}
