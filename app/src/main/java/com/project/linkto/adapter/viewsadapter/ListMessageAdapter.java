package com.project.linkto.adapter.viewsadapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
import com.project.linkto.bean.ChatMessage;
import com.project.linkto.bean.Person;
import com.project.linkto.fragment.message.ChatMessageFragment;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.sql.Timestamp;
import java.util.List;

import static com.project.linkto.BaseActivity.mDatabase;
import static com.project.linkto.fragment.BaseFragment.mActivity;

/**
 * Created by bbouzaiene on 21/05/2018.
 */

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.MyViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 7218;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 8127;
    private List<ChatMessage> chatMessageList;
    private Context mContext;

    public ListMessageAdapter(Context mContext, List<ChatMessage> chatMessageList) {
        this.mContext = mContext;
        this.chatMessageList = chatMessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_row, parent, false);*/

        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new MyViewHolder(view);
        }

        return null;
        //return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ChatMessage chatMessage = chatMessageList.get(position);
        holder.tv_body.setText(chatMessage.getMessageText());

        Timestamp currenttimestamp = new Timestamp(System.currentTimeMillis());
        holder.tv_date.setText(Utils.getdiffDate(currenttimestamp.toString(), chatMessage.getMessageTime()));
        holder.rl_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tv_date.getVisibility()==View.GONE)
                holder.tv_date.setVisibility(View.VISIBLE);
                else
                    holder.tv_date.setVisibility(View.GONE);
            }
        });
        mDatabase.child("users").child(chatMessage.getMessageUser()).addValueEventListener(new ValueEventListener() {
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

    private void drawPersonViews(ListMessageAdapter.MyViewHolder holder, final Person personProfile) {
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
            Picasso.with(mContext).load(personProfile.getProfilephoto()).resize(1000, 1000)
                    .centerCrop().transform(transformation)
                    .into(holder.profileimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return chatMessageList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_date;
        private final TextView tv_body;
        public ConstraintLayout rl_user;
        private TextView tv_title;
        public ImageView profileimg;

        public MyViewHolder(View view) {
            super(view);
            profileimg = (RoundedImageView) view.findViewById(R.id.profileimg);
            tv_title = (TextView) view.findViewById(R.id.text_message_name);
            tv_date = (TextView) view.findViewById(R.id.text_message_time);
            tv_body = (TextView) view.findViewById(R.id.text_message_body);
            rl_user = (ConstraintLayout) view.findViewById(R.id.rluser);
        }
    }

    @Override
    public int getItemViewType(int position) {

        final ChatMessage chatMessage = chatMessageList.get(position);
        if (chatMessage.getMessageUser().equals(DataHelper.getInstance().getmUserbd().getUid()))
            return VIEW_TYPE_MESSAGE_SENT;
        else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;

        }
    }
}
