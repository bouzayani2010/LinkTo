package com.project.linkto.adapter.pager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.linkto.Fragment.HomeFragment;
import com.project.linkto.Fragment.MyHomeFragment;

import java.util.List;

/**
 * Created by bbouzaiene on 14/05/2018.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> chapters;

    public MainPagerAdapter(FragmentManager childFragmentManager, List<String> chapters) {
        super(childFragmentManager);
        this.chapters = chapters;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyHomeFragment();
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        try {
            return chapters.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "" + this.chapters.get(position);
    }
}