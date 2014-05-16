package com.mw.smartoff.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mw.smartoff.fragments.MeetingAll;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		return new MeetingAll();
	}

	@Override
	public int getCount() {
		return 5;
	}

}
