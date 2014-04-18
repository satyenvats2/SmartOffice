package com.mw.smartoff.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mw.smartoffice.R;
import com.parse.ParseUser;

public class DashboardFragment extends Fragment {
	TextView welcomeDashTV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		welcomeDashTV = (TextView) getActivity().findViewById(R.id.welcome_dash_TV);

		ParseUser currentUser = ParseUser.getCurrentUser();
		welcomeDashTV.setText(currentUser.getUsername());
	}
	
	
	
}
