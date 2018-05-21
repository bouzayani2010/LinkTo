package com.project.linkto.adapter.viewsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.project.linkto.R;
import com.project.linkto.bean.GroupMessage;
import com.project.linkto.bean.Person;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import static com.project.linkto.BaseActivity.mDatabase;
import static com.project.linkto.fragment.BaseFragment.mActivity;

/**
 * Created by bbouzaiene on 21/05/2018.
 */

public class ListTitleMessageAdapter extends RecyclerView.Adapter<ListTitleMessageAdapter.MyViewHolder> {
    private List<GroupMessage> groupMessageList;
    private Context mContext;

    public ListTitleMessageAdapter(Context mContext, List<GroupMessage> groupMessageList) {
        this.mContext = mContext;
        this.groupMessageList = groupMessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_message_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GroupMessage groupMessage = groupMessageList.get(position);
        Log.i("groups", groupMessage.getListUserId().toString());
        String otherUserId = groupMessage.getListUserId().get(0);
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDatabase.child("users").child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person personProfile = dataSnapshot.getValue(Person.class);

                holder.tv_title.setText(personProfile.getFirstname() + " " + personProfile.getLastname());
                if (personProfile != null)
                    drawPersonViews(holder, personProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void drawPersonViews(ListTitleMessageAdapter.MyViewHolder holder, Person personProfile) {
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
            return groupMessageList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        public ImageView profileimg;

        public MyViewHolder(View itemView) {
            super(itemView);
            profileimg = (RoundedImageView) itemView.findViewById(R.id.profileimg);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
