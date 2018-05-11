package com.project.linkto.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.linkto.R;
import com.project.linkto.bean.Comment;

import java.util.List;

/**
 * Created by bbouzaiene on 20/04/2018.
 */

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.MyViewHolder> {


    private final List<Comment> commentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_comments;
        private TextView tv_shares;
        public TextView tv_likes;
        public TextView tv_title, tv_body, tv_date;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
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
    public void onBindViewHolder(ListCommentAdapter.MyViewHolder holder, int position) {
        try {
            final Comment commentFeed = commentList.get(position);
            holder.tv_body.setText(commentFeed.getContent_text());
            holder.tv_date.setText("" + commentFeed.getTimestamp());


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
}
