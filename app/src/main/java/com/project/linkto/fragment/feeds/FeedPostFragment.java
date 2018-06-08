package com.project.linkto.fragment.feeds;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.linkto.R;
import com.project.linkto.bean.Post;
import com.project.linkto.bean.Userbd;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.project.linkto.BaseActivity.storage;

/**
 * Created by bbouzaiene on 04/05/2018.
 */

public class FeedPostFragment extends BaseFragment {

    private static final int READ_REQUEST_CODE_IMAGE = 1005;
    private static final int READ_REQUEST_CODE_VIDEO = 1006;
    private static final int INDEX_VIDEO = 1;
    private static final int INDEX_PHOTO = 2;
    private Button bt_submit;
    private EditText ed_content_text;
    private DatabaseReference mDatabase;
    private Userbd userbd;
    private RelativeLayout media_actions;
    private VideoView videoshared;
    private ImageView imageshared;
    private Uri uriImageShared;
    private RelativeLayout video_actions;
    private Uri uriVideoshared;
    private int numLoad = 0;
    private int load = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedpost, container, false);
        mDatabase = mActivity.database.getReference();
        userbd = DataHelper.getInstance().getmUserbd();

        ed_content_text = (EditText) view.findViewById(R.id.ed_content_text);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        media_actions = (RelativeLayout) view.findViewById(R.id.media_actions);
        video_actions = (RelativeLayout) view.findViewById(R.id.video_actions);
        videoshared = (VideoView) view.findViewById(R.id.videoshared);

        imageshared = (ImageView) view.findViewById(R.id.imageshared);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load = 0;
                numLoad = 0;
                String content_text = ed_content_text.getText().toString();
                if (!Utils.isEmptyString(content_text)) {
                    if (uriImageShared == null && uriVideoshared == null) {
                        writeNewPost(userbd.getUid(), userbd.getDisplayName(), content_text.substring(0, 15), content_text, null, null);
                    } else {
                        if (uriImageShared != null) {
                            numLoad++;
                            loadNewProfilePhoto(uriImageShared);
                        }
                        if (uriVideoshared != null) {
                            numLoad++;
                            loadNewProfileVideo(uriVideoshared);
                        }
                    }
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


        media_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMediaFromLocalStorage(READ_REQUEST_CODE_IMAGE);
            }
        });
        video_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMediaFromLocalStorage(READ_REQUEST_CODE_VIDEO);
            }
        });
        return view;
    }

    private void loadNewProfilePhoto(Uri file) {
        StorageReference storageRef = storage.getReference();
        String md5 = Utils.getMD5EncryptedString(file.toString());
        StorageReference riversRef = null;
        riversRef = storageRef.child("post/" + md5);

        UploadTask uploadTask = riversRef.putFile(file);


        final StorageReference finalRiversRef = riversRef;
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    String content_text = ed_content_text.getText().toString();

                    loadNow(downloadUri, INDEX_PHOTO);
                    //  writeNewPost(userbd.getUid(), userbd.getDisplayName(), content_text.substring(0, 15), content_text, downloadUri.toString(), null);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void loadNewProfileVideo(Uri file) {
        StorageReference storageRef = storage.getReference();
        String md5 = Utils.getMD5EncryptedString(file.toString());
        StorageReference riversRef = null;
        riversRef = storageRef.child("post/" + md5);

        UploadTask uploadTask = riversRef.putFile(file);


        final StorageReference finalRiversRef = riversRef;
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    loadNow(downloadUri, INDEX_VIDEO);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void loadNow(Uri downloadUri, int index) {
        Uri downloadUriVideo = null;
        Uri downloadUriPhoto = null;
        String urlVideo = null;
        String urlPhoto = null;

        String content_text = ed_content_text.getText().toString();
        switch (index) {
            case INDEX_VIDEO:
                load++;
                downloadUriVideo = downloadUri;
                if (downloadUriVideo != null) {
                    urlVideo = downloadUriVideo.toString();
                }

                break;
            case INDEX_PHOTO:
                load++;
                downloadUriPhoto = downloadUri;
                if (downloadUriPhoto != null) {
                    urlPhoto = downloadUriPhoto.toString();
                }
                break;
            default:
                break;
        }

        if (load == numLoad) {
            writeNewPost(userbd.getUid(), userbd.getDisplayName(), content_text.substring(0, 15), content_text, urlPhoto, urlVideo);

        }
    }


    private void loadMediaFromLocalStorage(int load) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        switch (load) {
            case READ_REQUEST_CODE_IMAGE:
                intent.setType("image/*");
                break;
            case READ_REQUEST_CODE_VIDEO:
                intent.setType("video/*");
                break;
            default:
                break;
        }

        startActivityForResult(intent, load);
    }

    private void writeNewPost(String userId, String username, String title, String body, String urlPhoto, String urlVideo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body, timestamp.toString(), urlPhoto, urlVideo);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
        mActivity.onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if ((requestCode == READ_REQUEST_CODE_IMAGE) && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());

                try {
                    printImage(uri);

                    uriImageShared = uri;
                    //  loadNewProfilePhoto(requestCode, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((requestCode == READ_REQUEST_CODE_VIDEO) && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());

                try {
                    playVideo(uri);

                    uriVideoshared = uri;
                    //  loadNewProfilePhoto(requestCode, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void playVideo(Uri uri) {
        MediaController mediaControls = new MediaController(mActivity);

        videoshared.setVisibility(View.VISIBLE);
        try {
            //set the media controller in the VideoView
            videoshared.setMediaController(mediaControls);

            //videoshared the uri of the video to be played
            videoshared.setVideoURI(uri);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void printImage(Uri mUri) {
        Picasso.get()
                .load(mUri)
                .resize(1000, 1000)
                .centerCrop()

                // .centerCrop()
                .into(imageshared);

        imageshared.setVisibility(View.VISIBLE);

    }
}
