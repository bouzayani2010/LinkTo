package com.project.linkto.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
        final int[] icons = {
                R.drawable.ic_person,
                R.drawable.ic_home,
                R.drawable.ic_message,
                R.drawable.ic_diag
        };
        final int[] icons_gray = {
                R.drawable.ic_person_gray,
                R.drawable.ic_home_gray,
                R.drawable.ic_message_gray,
                R.drawable.ic_diag_gray
        };


        mainPagerAdapter = new MainPagerAdapter(getChildFragmentManager(), chapters, icons, icons_gray);
        viewPager.setAdapter(mainPagerAdapter);


        tabs.setViewPager(viewPager);
        LinearLayout view = (LinearLayout) tabs.getChildAt(0);
        ImageButton textView = (ImageButton) view.getChildAt(0);
        textView.setImageResource(icons[0]);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabs.
            }

            @Override
            public void onPageSelected(int position) {
                mainPagerAdapter.setCurrentPosition(position);
                LinearLayout view = (LinearLayout) tabs.getChildAt(0);
                for (int i = 0; i < view.getChildCount(); i++) {
                    ImageButton textView = (ImageButton) view.getChildAt(i);
                    textView.setImageResource(icons_gray[i]);
                }
                ImageButton textView = (ImageButton) view.getChildAt(position);
                textView.setImageResource(icons[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public ViewPager getViewPager() {
        return viewPager;
    }


}
