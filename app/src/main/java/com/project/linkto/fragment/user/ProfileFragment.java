package com.project.linkto.fragment.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
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
    private RelativeLayout header1_rl;
    private RelativeLayout header2_rl;
    private RelativeLayout header3_rl;
    private LinearLayout body1;
    private LinearLayout body2;
    private LinearLayout body3;
    private ImageView img_arrow1;
    private ImageView img_arrow2;
    private ImageView img_arrow3;
    private EditText name_edit;
    private EditText lastname_edit;
    private EditText email_edit;
    private EditText country_edit;
    private SimpleDateFormat simpleDateFormat;
    private Spinner gender_text;
    private Button bt_submit;
    private Person personProfile;
    private EditText city_edit;
    private EditText note_edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.from(mActivity).inflate(R.layout.fragment_profile, container, false);

        mDatabase = mActivity.database.getReference();
        userName = (TextView) view.findViewById(R.id.nameuser);
        coverImg = (ImageView) view.findViewById(R.id.coverimg);
        profileImg = (ImageView) view.findViewById(R.id.profileimg);
        close_button = (ImageView) view.findViewById(R.id.close_button);
        birthdate_text = (TextView) view.findViewById(R.id.birthdate_text);


        header1_rl = view.findViewById(R.id.header1);
        header2_rl = view.findViewById(R.id.header2);
        header3_rl = view.findViewById(R.id.header3);
        body1 = view.findViewById(R.id.body1);
        body2 = view.findViewById(R.id.body2);
        body3 = view.findViewById(R.id.body3);


        img_arrow1 = view.findViewById(R.id.img_arrow1);
        img_arrow2 = view.findViewById(R.id.img_arrow2);
        img_arrow3 = view.findViewById(R.id.img_arrow3);


        name_edit = view.findViewById(R.id.name_edit_text);
        lastname_edit = view.findViewById(R.id.lastname_edit_text);
        email_edit = view.findViewById(R.id.email_edit_text);
        country_edit = view.findViewById(R.id.country_edit_text);
        city_edit = view.findViewById(R.id.city_edit_text);
        birthdate_text = view.findViewById(R.id.birthdate_text);
        gender_text = view.findViewById(R.id.name_gender_text);
        note_edit=view.findViewById(R.id.note_edit_text);
        bt_submit = view.findViewById(R.id.bt_submit);

        header1_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (body1.getVisibility() == View.VISIBLE) {
                    body1.setVisibility(View.GONE);
                    img_arrow1.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    body1.setVisibility(View.VISIBLE);
                    img_arrow1.setImageResource(R.drawable.ic_arrow_top);
                }
            }
        });
        header2_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (body2.getVisibility() == View.VISIBLE) {
                    body2.setVisibility(View.GONE);
                    img_arrow2.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    body2.setVisibility(View.VISIBLE);
                    img_arrow2.setImageResource(R.drawable.ic_arrow_top);
                }
            }
        });
        header3_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (body3.getVisibility() == View.VISIBLE) {
                    body3.setVisibility(View.GONE);
                    img_arrow3.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    body3.setVisibility(View.VISIBLE);
                    img_arrow3.setImageResource(R.drawable.ic_arrow_top);
                }
            }
        });


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

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        simpleDateFormat = new SimpleDateFormat("dd MMM yyy");
        return view;
    }

    private void updateProfile() {

        personProfile.setFirstname(name_edit.getText().toString());
        personProfile.setLastname(lastname_edit.getText().toString());
        personProfile.setCountry(country_edit.getText().toString());
        personProfile.setGender(gender_text.getSelectedItem().toString());
        personProfile.setCity(city_edit.getText().toString());
        personProfile.setSummary(note_edit.getText().toString());
        String uidProfile = DataHelper.getInstance().getmUserbd().getUid();
        mDatabase.child("users").child(uidProfile).setValue(personProfile);
    }


    private void drawPersonViews(Person personProfile) {
        try {
            Log.i("personperson", personProfile.getCoverphoto());
            Log.i("person", personProfile.getProfilephoto());
            userName.setText(personProfile.getFirstname() + " " + personProfile.getLastname());
            name_edit.setText(personProfile.getFirstname());
            lastname_edit.setText(personProfile.getLastname());
            email_edit.setText(personProfile.getEmail());
            Date birthdate = new Date(personProfile.getBirthdate());
            birthdate_text.setText("" + simpleDateFormat.format(birthdate));
            country_edit.setText(personProfile.getCountry());
            String gender = personProfile.getGender();
            if (gender.equalsIgnoreCase("male")) {
                gender_text.setSelection(0);
            } else {
                gender_text.setSelection(1);
            }

            city_edit.setText(personProfile.getCity());
            note_edit.setText(personProfile.getSummary());
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

    public void showDatePicker(View v) {
        DialogFragment dFragment = new DatePickerFragment();

        // Show the date picker dialog fragment
        dFragment.show(mActivity.getFragmentManager(), "Date Picker");
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
                        personProfile = dataSnapshot.getValue(Person.class);
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
    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                Initialize a new DatePickerDialog

                DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                    int year, int monthOfYear, int dayOfMonth)
             */
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            return dpd;
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
