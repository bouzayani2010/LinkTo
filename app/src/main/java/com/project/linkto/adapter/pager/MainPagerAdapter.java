package com.project.linkto.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.project.linkto.fragment.chapter.HomeFragment;
import com.project.linkto.fragment.chapter.MyHomeFragment;
import com.project.linkto.fragment.message.ChatListMessageFragment;

import java.util.List;

/**
 * Created by bbouzaiene on 14/05/2018.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private final List<String> chapters;
    private final int[] icons;

    public MainPagerAdapter(FragmentManager childFragmentManager, List<String> chapters, int[] icons) {
        super(childFragmentManager);
        this.chapters = chapters;
        this.icons = icons;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyHomeFragment();
            case 1:
                return new HomeFragment();
            case 2:
                return new ChatListMessageFragment();
            case 3:
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

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + this.chapters.get(position);
    }

    @Override
    public int getPageIconResId(int position) {
        return this.icons[position];
    }
}
