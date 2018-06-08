package com.project.linkto.adapter.viewsadapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.project.linkto.R;
import com.project.linkto.bean.Like;
import com.project.linkto.bean.Person;
import com.project.linkto.bean.Post;
import com.project.linkto.fragment.MainFragment;
import com.project.linkto.fragment.feeds.CommentFragment;
import com.project.linkto.fragment.message.ChatMessageFragment;
import com.project.linkto.singleton.DataFilter;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.project.linkto.BaseActivity.mDatabase;
import static com.project.linkto.fragment.BaseFragment.mActivity;

/**
 * Created by bbouzaiene on 20/04/2018.
 */

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.MyViewHolder> {


    private final List<Post> postList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private  FrameLayout video_layout;
        private  UniversalMediaController mMediaController;
        private ImageView imageshared;
        private RelativeLayout bodyshared;
        private RelativeLayout rl_user;
        private RoundedImageView profileimg;
        private TextView tv_comments;
        private TextView tv_shares;
        public TextView tv_likes;
        private UniversalVideoView videoshared;
        public TextView tv_title, tv_body, tv_date;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            profileimg = (RoundedImageView) view.findViewById(R.id.profileimg);
            imageshared = (ImageView) view.findViewById(R.id.imageshared);
            videoshared = (UniversalVideoView) view.findViewById(R.id.videoshared);
            tv_body = (TextView) view.findViewById(R.id.tv_body);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_likes = (TextView) view.findViewById(R.id.tv_likes);
            tv_comments = (TextView) view.findViewById(R.id.tv_comments);
            tv_shares = (TextView) view.findViewById(R.id.tv_shares);
            rl_user = (RelativeLayout) view.findViewById(R.id.rl_user);
            bodyshared = (RelativeLayout) view.findViewById(R.id.bodyshared);
            mMediaController = (UniversalMediaController) view.findViewById(R.id.media_controller);
            video_layout = (FrameLayout) view.findViewById(R.id.video_layout);
        }
    }

    public ListPostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public ListPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    private void playVideo(MyViewHolder holder, String url) {
        MediaController mediaControls = new MediaController(mActivity);

        holder.videoshared.setVisibility(View.VISIBLE);
        try {
            //set the media controller in the VideoView
            holder.videoshared.setMediaController(holder.mMediaController);
            //videoshared the uri of the video to be played
            Uri uri = Uri.parse(url);
            holder.videoshared.setVideoURI(uri);
            holder.videoshared.start();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onBindViewHolder(final ListPostAdapter.MyViewHolder holder, int position) {
        try {
            final Post post = postList.get(position);
            holder.tv_title.setText(post.getTitle());
            holder.tv_body.setText(post.getBody());

            final Timestamp currenttimestamp = new Timestamp(System.currentTimeMillis());
            holder.tv_date.setText(Utils.getdiffDate(currenttimestamp.toString(), post.getTimestamp()));
            final String userId = DataHelper.getInstance().getmUserbd().getUid();

            if (!Utils.isEmptyString(post.getUrlPhoto())) {
                Picasso.get().load(post.getUrlPhoto()).resize(1000, 1000)
                        .centerCrop()
                        .into(holder.imageshared);
            }
            if (!Utils.isEmptyString(post.getUrlVideo())) {
                playVideo(holder, post.getUrlVideo());
                holder.video_layout.setVisibility(View.GONE);
            } else {
                holder.video_layout.setVisibility(View.GONE);
            }

            Log.i("yeardaymonth1", post.getTimestamp().toString());
            if (!Utils.isEmptyString(post.getOriginPostId())) {
                holder.tv_body.setVisibility(View.GONE);
                holder.imageshared.setVisibility(View.GONE);
                holder.video_layout.setVisibility(View.GONE);


                LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                View schedule_view = inflater.inflate(R.layout.post_list_row, holder.bodyshared, false);
                // ((TextView)schedule_view).setText(list_schedule.get(i));
                holder.bodyshared.addView(schedule_view);
                final MyViewHolder holderShared = new MyViewHolder(schedule_view);
                mDatabase.child("posts").child(post.getOriginPostId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Post postShared = dataSnapshot.getValue(Post.class);
                        if (postShared != null) {
                            holderShared.tv_title.setText(postShared.getTitle());
                            holderShared.tv_body.setText(postShared.getBody());
                            holderShared.tv_likes.setText("" + postShared.getStarCount() + " likes");
                            holderShared.tv_comments.setText("" + postShared.getCommentCount() + " comments");
                            holderShared.tv_shares.setText("" + postShared.getShareCount() + " shares");
                            holderShared.tv_date.setText(Utils.getdiffDate(currenttimestamp.toString(), postShared.getTimestamp()));

                            if (!Utils.isEmptyString(postShared.getUrlPhoto())) {
                                Picasso.get().load(postShared.getUrlPhoto()).resize(1000, 1000)
                                        .centerCrop()
                                        .into(holderShared.imageshared);
                            }
                            holderShared.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    postShared.setKey(post.getOriginPostId());
                                    CommentFragment commentFragment = new CommentFragment(postShared);
                                    //     commentFragment.setPost(postShared);
                                    mActivity.gotoComment(commentFragment);
                                    // mActivity.gotoFeedPost();
                                }
                            });

                            mDatabase.child("users").child(postShared.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Person personProfile = dataSnapshot.getValue(Person.class);
                                    if (personProfile != null)
                                        drawPersonViews(holderShared, personProfile);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                holder.tv_body.setVisibility(View.VISIBLE);
                holder.video_layout.setVisibility(View.VISIBLE);
                holder.imageshared.setVisibility(View.VISIBLE);
                holder.bodyshared.setVisibility(View.GONE);
            }
            mDatabase.child("users").child(post.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Person personProfile = dataSnapshot.getValue(Person.class);
                    if (personProfile != null)
                        drawPersonViews(holder, personProfile);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.rl_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(mActivity)
                            .title(R.string.Profile)
                            .items(R.array.activity_profile)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    switch (which) {
                                        case 0:
                                            break;
                                        case 1:
                                            MainFragment.viewPager.setCurrentItem(2);
                                            ChatMessageFragment chatMessageFragment = new ChatMessageFragment();
                                            chatMessageFragment.setmUserId(post.getUid());
                                            mActivity.gotoChatMessage(chatMessageFragment);
                                            break;
                                    }
                                }
                            })
                            .show();
                }
            });
            holder.tv_likes.setText("" + post.getStarCount() + " likes");
            holder.tv_comments.setText("" + post.getCommentCount() + " comments");
            holder.tv_shares.setText("" + post.getShareCount() + " shares");
            holder.tv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentFragment commentFragment = new CommentFragment(post);
                    //    commentFragment.setPost(post);
                    mActivity.gotoComment(commentFragment);
                    // mActivity.gotoFeedPost();
                }
            });


            holder.tv_shares.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(mActivity)
                            .title(R.string.share)
                            .content(R.string.share_post)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();

                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    String key = mDatabase.child("posts").push().getKey();
                                    Post post1 = new Post(userId, post.getAuthor(), post.getTitle(), post.getBody(), timestamp.toString(), post.getUrlPhoto(), post.getUrlVideo());
                                    if (Utils.isEmptyString(post.getOriginPostId())) {
                                        post1.setOriginPostId(post.getKey());
                                    } else {
                                        post1.setOriginPostId(post.getOriginPostId());
                                    }
                                    Map<String, Object> postValues = post1.toMap();

                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("/posts/" + key, postValues);
                                    mDatabase.updateChildren(childUpdates);


                                    mDatabase.child("posts").child(post.getKey()).child("shareCount").setValue(post.getShareCount() + 1);
                                    notifyDataSetChanged();
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
            final String myLikeKey = DataFilter.getInstance().liked(post, userId);
            if (myLikeKey != null) {
                holder.tv_likes.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
                //  holder.tv_likes.setEnabled(false);
            } else {
                holder.tv_likes.setTextColor(mActivity.getResources().getColor(R.color.colorHint));
                // holder.tv_likes.setEnabled(true);
            }

            holder.tv_likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myLikeKey != null) {
                        new MaterialDialog.Builder(mActivity)
                                .title(R.string.like)
                                .content(R.string.remove_like)
                                .positiveText(R.string.ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        mDatabase.child("posts").child(post.getKey()).child("starCount").setValue(post.getStarCount() - 1);
                                        mDatabase.child("posts").child(post.getKey()).child("likes").child(myLikeKey).removeValue();//.setValue(null);
                                        notifyDataSetChanged();
                                    }
                                })
                                .negativeText(R.string.cancel)
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {

                        String key = mDatabase.child("posts").child(post.getKey()).push().getKey();
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        Like like = new Like(userId, post.getKey(), timestamp.toString());
                        Map<String, Object> childLikes = new HashMap<>();
                        childLikes.put("/likes/" + key, like.toMap());
                        mDatabase.child("posts").child(post.getKey()).updateChildren(childLikes);
                        mDatabase.child("posts").child(post.getKey()).child("starCount").setValue(post.getStarCount() + 1);
                    }
                    notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void drawPersonViews(MyViewHolder holder, Person personProfile) {
        try {
            Log.i("person", personProfile.getCoverphoto());
            Log.i("person", personProfile.getProfilephoto());
            holder.tv_title.setText(personProfile.getFirstname() + " " + personProfile.getLastname());
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(mActivity.getResources().getColor(R.color.colorAccent))
                    .borderWidthDp(3)
                    .cornerRadiusDp(20)
                    .oval(false)
                    .build();
            Picasso.get().load(personProfile.getProfilephoto()).resize(1000, 1000)
                    .centerCrop().transform(transformation)
                    .into(holder.profileimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return postList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
