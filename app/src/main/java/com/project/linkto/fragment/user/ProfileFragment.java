package com.project.linkto.fragment.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.project.linkto.R;
import com.project.linkto.bean.Person;
import com.project.linkto.customwidget.MyDatePickerFragment;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.GlideApp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.project.linkto.BaseActivity.mDatabase;

public class ProfileFragment extends BaseFragment {

    private TextView userName;
    private ImageView coverImg;
    private ImageView profileImg;
    private ImageView close_button;
    private TextView birthdate_text;
    private int year, month, day;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.from(mActivity).inflate(R.layout.fragment_profile, container, false);

        userName = (TextView) view.findViewById(R.id.nameuser);
        coverImg = (ImageView) view.findViewById(R.id.coverimg);
        profileImg = (ImageView) view.findViewById(R.id.profileimg);
        close_button = (ImageView) view.findViewById(R.id.close_button);
        birthdate_text = (TextView) view.findViewById(R.id.birthdate_text);


        birthdate_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment.this.onBackPressed();
            }
        });
        return view;
    }


    public void showDatePicker(View v) {
        DialogFragment dFragment = new DatePickerFragment();

        // Show the date picker dialog fragment
        dFragment.show(mActivity.getFragmentManager(), "Date Picker");
    }


    private void drawPersonViews(Person personProfile) {
        try {
            Log.i("personperson", personProfile.getCoverphoto());
            Log.i("person", personProfile.getProfilephoto());
            userName.setText(personProfile.getFirstname() + " " + personProfile.getLastname());


            GlideApp.with(mActivity)
                    .load(personProfile.getProfilephoto())
                    // .load("http://via.placeholder.com/300.png")
                    .override(300, 300)
                    .centerCrop()
                    .into(profileImg);


            GlideApp.with(mActivity)
                    .load(personProfile.getCoverphoto())
                    .override(1000, 500)
                    .centerCrop()
                    .into(coverImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (DataHelper.getInstance().isConnected()) {
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
        }
    }

    @SuppressLint("ValidFragment")
    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                Initialize a new DatePickerDialog

                DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                    int year, int monthOfYear, int dayOfMonth)
             */
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);
            return  dpd;
        }
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formattedDate = df.format(chosenDate);

            // Display the chosen date to app interface
            birthdate_text.setText(formattedDate);
        }
    }
}
