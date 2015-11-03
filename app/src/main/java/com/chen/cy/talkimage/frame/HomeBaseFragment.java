package com.chen.cy.talkimage.frame;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.activity.MainActivity;
import com.chen.cy.talkimage.adapter.MainPagerAdapter;
import com.chen.cy.talkimage.views.BaseFrame;
import com.chen.cy.talkimage.views.Fab;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeBaseFragment extends BaseFrame implements TabLayout.OnTabSelectedListener{

    @Bind(R.id.home_base_tabView)
    TabLayout homeBaseTabView;
    @Bind(R.id.home_base_viewPager)
    ViewPager homeBaseViewPager;

    private MainActivity mainActivity;

    private HomeHotFrame homeHotFrame;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_base, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity)getActivity();
        homeBaseViewPager.setAdapter(new MainPagerAdapter(getActivity(), getFragmentManager()));
        homeBaseViewPager.setOffscreenPageLimit(MainPagerAdapter.NUM_ITEMS);
        updatePage(homeBaseViewPager.getCurrentItem());

        // Setup tab layout
        homeBaseTabView.setupWithViewPager(homeBaseViewPager);
        homeBaseViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                updatePage(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        return view;
    }

    private void updatePage(int selectedPage) {
        updateFab(selectedPage);
    }

    private void updateFab(int selectedPage) {
        switch (selectedPage) {
            case MainPagerAdapter.HOT_POS:
                mainActivity.showFab();
                HomeHotFrame.fabInScreen = true;
                break;
            case MainPagerAdapter.ATTENTION_POS:
                mainActivity.hideFab();
                break;
            case MainPagerAdapter.NEARBY_POS:
                mainActivity.showFab();
            default:
                break;
        }
    }

    private void clearFabClickLisener(Fab fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}