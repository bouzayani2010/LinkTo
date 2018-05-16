package com.project.linkto.adapter.viewsadapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.project.linkto.R;
import com.project.linkto.bean.Comment;
import com.project.linkto.bean.Person;
import com.project.linkto.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.sql.Timestamp;
import java.util.List;

import static com.project.linkto.BaseActivity.mDatabase;
import static com.project.linkto.fragment.BaseFragment.mActivity;

/**
 * Created by bbouzaiene on 20/04/2018.
 */

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.MyViewHolder> {


    private final List<Comment> commentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView profileimg;
        private TextView tv_comments;
        private TextView tv_shares;
        public TextView tv_likes;
        public TextView tv_title, tv_body, tv_date;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            profileimg = (RoundedImageView) view.findViewById(R.id.profileimg);
            tv_body = (TextView) view.findViewById(R.id.tv_body);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_likes = (TextView) view.findViewById(R.id.tv_likes);
            tv_comments = (TextView) view.findViewById(R.id.tv_comments);
            tv_shares = (TextView) view.findViewById(R.id.tv_shares);
        }
    }

    public ListCommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public ListCommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListCommentAdapter.MyViewHolder holder, int position) {
        try {
            final Comment commentFeed = commentList.get(position);
            holder.tv_body.setText(commentFeed.getContent_text());


            Timestamp currenttimestamp = new Timestamp(System.currentTimeMillis());
            holder.tv_date.setText(Utils.getdiffDate(currenttimestamp.toString(), commentFeed.getTimestamp()));
            // holder.tv_date.setText("" + commentFeed.getTimestamp());
            mDatabase.child("users").child(commentFeed.getUid()).addValueEventListener(new ValueEventListener() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        try {
            return commentList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void drawPersonViews(ListCommentAdapter.MyViewHolder holder, Person personProfile) {
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
}
