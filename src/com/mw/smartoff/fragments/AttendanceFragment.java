package com.mw.smartoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mw.smartoffice.R;

//import android.app.Fragment;

public class AttendanceFragment extends Fragment {
	Button markAttendanceButton;
	Button checkAttendanceButton;

	private void findThings() {
		markAttendanceButton = (Button) getActivity().findViewById(
				R.id.mark_attendance_B);
		checkAttendanceButton = (Button) getActivity().findViewById(
				R.id.check_attendance_B);
	}

	private void initThings() {
	}

	private void myOwnListeners() {

		markAttendanceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "29", Toast.LENGTH_SHORT).show();
			}
		});

		checkAttendanceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "59", Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.attendance_fragment,
				container, false);
		return rootView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();
		myOwnListeners();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
