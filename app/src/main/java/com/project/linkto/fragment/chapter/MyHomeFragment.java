package com.project.linkto.fragment.chapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.project.linkto.R;
import com.project.linkto.adapter.viewsadapter.ListPostAdapter;
import com.project.linkto.bean.Person;
import com.project.linkto.bean.Post;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.project.linkto.BaseActivity.mDatabase;
import static com.project.linkto.BaseActivity.storage;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyHomeFragment extends BaseFragment {

    private static final int READ_REQUEST_CODE_profile = 19;
    private static final int READ_REQUEST_CODE_cover = 119;
    private TextView userName;
    private List<Post> postList = new ArrayList<Post>();
    private RecyclerView recyclerView;
    public ListPostAdapter mAdapter;
    private ImageView coverImg;
    private ImageView profileImg;
    private ImageView logoutImg;
    private FloatingActionButton fab;

    public MyHomeFragment() {
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
        coverImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> coverprofile_activity = Arrays.asList(getResources().getStringArray(R.array.coverprofile));
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.profile)
                        .items(coverprofile_activity)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        //   loadNewProfilePhoto();
                                        loadPhotoFromLocalStorage(READ_REQUEST_CODE_cover);
                                        break;
                                    case 1:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> profile_activity = Arrays.asList(getResources().getStringArray(R.array.profile));
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.profile)
                        .items(profile_activity)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        //   loadNewProfilePhoto();
                                        loadPhotoFromLocalStorage(READ_REQUEST_CODE_profile);
                                        break;
                                    case 1:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
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
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //mDatabase.child("users").

        String uidProfile = DataHelper.getInstance().getmUserbd().getUid();
        DatabaseReference ref = mDatabase.child("posts").getRef();
        Query query = ref.orderByChild("uid").equalTo(uidProfile);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("mamama1", "1::" + dataSnapshot.toString());
                try {
                    postList.clear();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.i("mamama1", "::" + singleSnapshot.toString());
                        Post post = singleSnapshot.getValue(Post.class);
                        post.setKey(singleSnapshot.getKey());
                        //
                        postList.add(post);
                        Log.i("mamama1", "::" + post.toString());

                    }
                } catch (Exception e) {
                    Log.i("mamama", "::" + e.getMessage());
                    e.printStackTrace();
                }
                Collections.sort(postList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadPhotoFromLocalStorage(int load) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, load);
    }

    private void loadNewProfilePhoto(final int requestCode, Uri file) {

        StorageReference storageRef = storage.getReference();
        String md5 = Utils.getMD5EncryptedString(file.toString());
        StorageReference riversRef = null;
        if (requestCode == READ_REQUEST_CODE_profile)
            riversRef = storageRef.child("profile/profile/" + md5);
        else if (requestCode == READ_REQUEST_CODE_cover)
            riversRef = storageRef.child("profile/cover/" + md5);
        UploadTask uploadTask = riversRef.putFile(file);


        final StorageReference finalRiversRef = riversRef;
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return finalRiversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i("uriuri", "1** " + downloadUri.toString());

                    addUrltoFirebase(requestCode, downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

        /*Uri downloadUri = urlTask.getResult();
        Log.i("uriuri",downloadUri.toString());*/


    }

    private void addUrltoFirebase(int requestCode, String newurlPhoto) {

        String uidProfile = DataHelper.getInstance().getmUserbd().getUid();
        if (requestCode == READ_REQUEST_CODE_profile) {
            mDatabase.child("users").child(uidProfile).child("profilephoto").setValue(newurlPhoto);
        } else if (requestCode == READ_REQUEST_CODE_cover) {
            mDatabase.child("users").child(uidProfile).child("coverphoto").setValue(newurlPhoto);
        }
    }

    private void drawPersonViews(Person personProfile) {
        try {
            Log.i("person", personProfile.getCoverphoto());
            Log.i("person", personProfile.getProfilephoto());
            userName.setText(personProfile.getFirstname() + " " + personProfile.getLastname());

            Glide.with(mActivity).load(personProfile.getCoverphoto()).into(coverImg);
            Glide.with(mActivity).load(personProfile.getProfilephoto())
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if ((requestCode == READ_REQUEST_CODE_profile || requestCode == READ_REQUEST_CODE_cover) && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());

                try {
                    loadNewProfilePhoto(requestCode, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
