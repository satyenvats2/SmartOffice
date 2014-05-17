package com.mw.smartoff.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mw.smartoff.DisplayAttendanceActivity;
import com.mw.smartoff.DAO.AttendanceDAO;
import com.mw.smartoffice.R;

//import android.app.Fragment;

public class AttendanceFragment extends Fragment {
	Button markAttendanceButton;
	Button checkAttendanceButton;
	AttendanceDAO dao;
	Intent nextIntent;

	private void findThings() {
		markAttendanceButton = (Button) getActivity().findViewById(
				R.id.mark_attendance_B);
		checkAttendanceButton = (Button) getActivity().findViewById(
				R.id.check_attendance_B);
	}

	private void initThings() {
		dao = new AttendanceDAO(getActivity());
	}

	private void myOwnListeners() {

		markAttendanceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dao.markYourAttendance();
			}
		});

		checkAttendanceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "59", Toast.LENGTH_SHORT).show();
				nextIntent = new Intent(getActivity(), DisplayAttendanceActivity.class);
				startActivity(nextIntent);
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

}
