package com.project.linkto.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.linkto.R;
import com.project.linkto.bean.Post;

import java.util.List;

/**
 * Created by bbouzaiene on 20/04/2018.
 */

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.MyViewHolder> {


    private final List<Post> postList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_body, tv_date;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_body = (TextView) view.findViewById(R.id.tv_body);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
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

    @Override
    public void onBindViewHolder(ListPostAdapter.MyViewHolder holder, int position) {
        try {
            Post post = postList.get(position);
            holder.tv_title.setText(post.getTitle());
            holder.tv_body.setText(post.getBody());/*
            Date birthDate=new Date(post.getTimestamp());
            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY MMM DD");*/
            holder.tv_date.setText("" + post.getTimestamp());
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
