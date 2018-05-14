package com.project.linkto.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

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

    public void onBackPressed() {
        if (this instanceof MainFragment) {
            MainFragment mainFrgment = (MainFragment) this;
            int positionViewPager = mainFrgment.getViewPager().getCurrentItem();
            if (positionViewPager< 1) {
                mActivity.finish();
            }else{
                mainFrgment.getViewPager().setCurrentItem(positionViewPager-1);
            }
        } else {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.e("MainActivity", "popping backstack");
                fm.popBackStack();
            } else {
                Log.e("MainActivity", "nothing off backstack, calling super");
                (mActivity).finish();
            }
            Log.d("onBackPressed", "BaseFragment");
            //mActivity.finish();
        }
    }
}
