package com.project.linkto.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.project.linkto.MainActivity;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class BaseFragment extends Fragment {
    public static MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) this.getActivity();
    }
}
