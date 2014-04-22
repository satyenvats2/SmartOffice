package com.mw.smartoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mw.smartoffice.R;

public class TestFragment extends Fragment {
	
	public static Fragment newInstance() {
		
        TestFragment fragment = new TestFragment();

        return fragment;
    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.test_fragment, container, false);
		return rootView;
	}
	
}
