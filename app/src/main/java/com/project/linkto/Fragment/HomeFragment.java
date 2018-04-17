package com.project.linkto.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.linkto.R;
import com.project.linkto.singleton.DataHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private TextView userName;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        userName=(TextView)view.findViewById(R.id.nameuser);
        if(DataHelper.getInstance().isConnected()){
            userName.setText(DataHelper.getInstance().getmUser().getEmail());
        }
        return view;

    }
}
