package com.project.linkto.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.linkto.R;
import com.project.linkto.bean.Person;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bbouzaiene on 20/04/2018.
 */

public class ListPersonAdapter extends RecyclerView.Adapter<ListPersonAdapter.MyViewHolder> {


    private final List<Person> personList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_firstname, tv_lastname, tv_birthdate;

        public MyViewHolder(View view) {
            super(view);
            tv_firstname = (TextView) view.findViewById(R.id.tv_firstname);
            tv_lastname = (TextView) view.findViewById(R.id.tv_lastname);
            tv_birthdate = (TextView) view.findViewById(R.id.tv_birthdate);
        }
    }

    public ListPersonAdapter(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public ListPersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListPersonAdapter.MyViewHolder holder, int position) {
        try {
            Person person = personList.get(position);
            holder.tv_firstname.setText(person.getFirstname());
            holder.tv_lastname.setText(person.getLastname());
            Date birthDate=new Date(person.getBirthdate());
            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY MMM DD");
            holder.tv_birthdate.setText(""+dateFormat.format(birthDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        try {
            return personList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
