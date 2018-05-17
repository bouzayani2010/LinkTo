package com.project.linkto.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.project.linkto.R;
import com.project.linkto.adapter.pager.MainPagerAdapter;

import java.util.Arrays;
import java.util.List;


/**
 * Created by bbouzaiene on 14/05/2018.
 */

public class MainFragment extends BaseFragment {
    private MainPagerAdapter mainPagerAdapter;
    public static ViewPager viewPager;
    public static PagerSlidingTabStrip tabs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.vpPager);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

        drawViews();
        return view;
    }

    private void drawViews() {
        List<String> chapters = Arrays.asList(getResources().getStringArray(R.array.chapter));

        mainPagerAdapter = new MainPagerAdapter(getChildFragmentManager(),chapters);
        viewPager.setAdapter(mainPagerAdapter);

        tabs.setViewPager(viewPager);

    }

    public ViewPager getViewPager() {
        return viewPager;
    }


}
