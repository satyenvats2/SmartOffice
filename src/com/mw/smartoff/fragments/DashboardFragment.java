package com.mw.smartoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mw.smartoffice.R;
import com.viewpagerindicator.TabPageIndicator;

//public class DashboardFragment extends Fragment {
//	TextView welcomeDashTV;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
//		return rootView;
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		
//		super.onViewCreated(view, savedInstanceState);
//		welcomeDashTV = (TextView) getActivity().findViewById(R.id.welcome_dash_TV);
//
//		ParseUser currentUser = ParseUser.getCurrentUser();
//		welcomeDashTV.setText("Welcome : " + currentUser.getUsername());
//	}
//	
//	
//	
//}

public class DashboardFragment extends Fragment {
	TextView welcomeDashTV;
	private static final String[] CONTENT = new String[] { "Recent", "Artists",
			"Albums", "Songs", "Playlists", "Genres" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dashboard_fragment,
				container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getActivity()
				.getSupportFragmentManager());

		// FragmentPagerAdapter adapter = new
		// TabsPagerAdapter(this.getFragmentManager());
		// FragmentPagerAdapter adapter = new
		// TabsPagerAdapter(getSupportFragmentManager());
		// FragmentPagerAdapter adapter = new
		// TabsPagerAdapter(getChildFragmentManager());

		ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) getActivity()
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);

	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(
				android.support.v4.app.FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			switch (position) {
	        case 0:
				return new TestFragment();
	        case 1:
				return new TestFragment2();
	        case 2:
				return new TestFragment();
	        	
	        case 3:
				return new TestFragment();
	        }
			return new TestFragment();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}
		
		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

}