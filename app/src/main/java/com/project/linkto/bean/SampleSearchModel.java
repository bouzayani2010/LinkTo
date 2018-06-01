package com.project.linkto.bean;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by bbouzaiene on 31/05/2018.
 */

public class SampleSearchModel implements SearchSuggestion {
    private String mTitle;
    private Person person;

    public SampleSearchModel(String title) {
        mTitle = title;
    }



    @Override
    public String getBody() {
        return mTitle.toLowerCase();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
