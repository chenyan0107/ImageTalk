package com.chen.cy.talkimage.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.frame.HomeAttentionFrame;
import com.chen.cy.talkimage.frame.HomeHotFrame;
import com.chen.cy.talkimage.frame.HomeNearbyFrame;


/**
 * Created by Gordon Wong on 7/17/2015.
 *
 * Pager adapter for main activity.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

	public static final int NUM_ITEMS = 3;
	public static final int HOT_POS = 0;
	public static final int ATTENTION_POS = 1;
	public static final int NEARBY_POS = 2;

	private Context context;

	public MainPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case HOT_POS:
			return HomeHotFrame.newInstance();
		case ATTENTION_POS:
			return HomeAttentionFrame.newInstance();
		case NEARBY_POS:
			return HomeNearbyFrame.newInstance();
		default:
			return null;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case HOT_POS:
			return context.getString(R.string.hot);
		case ATTENTION_POS:
			return context.getString(R.string.attention);
		case NEARBY_POS:
			return context.getString(R.string.nearby);
		default:
			return "";
		}
	}

	@Override
	public int getCount() {
		return NUM_ITEMS;
	}
}
