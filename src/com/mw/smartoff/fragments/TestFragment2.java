package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;

public class TestFragment2 extends Fragment {
	ListView meetingLV;
	TextView notifyMeetingTV;

	GlobalVariable globalVariable;

	Intent nextIntent;
	MeetingsAdapter adapter;

	CreateDialog createDialog;
//	ProgressDialog progressDialog;
	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	List<Meeting> meetingList = new ArrayList<Meeting>();
	List<Meeting> tempMeetingList;

	private void findThings() {
		meetingLV = (ListView) getActivity().findViewById(R.id.meeting_LV2);
		notifyMeetingTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		createDialog = new CreateDialog(getActivity());
//		progressDialog = createDialog.createProgressDialog("Loading",
//				"Fetching Meetings", true, null);
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Toast.makeText(getActivity(), "onCreateView", Toast.LENGTH_SHORT)
				.show();

		View rootView = inflater.inflate(R.layout.test_fragment2, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Toast.makeText(getActivity(), "onViewCreated", Toast.LENGTH_SHORT)
				.show();

		findThings();
		initThings();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			Toast.makeText(getActivity(), "%%%% UserVisible true",
					Toast.LENGTH_SHORT).show();
			System.out.println("%%%% UserVisible true");
			tempMeetingList = globalVariable.getMeetingList();
			for (int i = 0; i < tempMeetingList.size(); i++) {
				if (!tempMeetingList.get(i).isHasBeenResponsedTo())
					meetingList.add(tempMeetingList.get(i));
			}
			if (meetingList.size() == 0) {
				notifyMeetingTV.setText("No meetings found");
				notifyMeetingTV.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "else" + meetingList.size(),
						Toast.LENGTH_SHORT).show();
				adapter = new MeetingsAdapter(getActivity(), meetingList);
				meetingLV.setAdapter(adapter);

				meetingLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						nextIntent = new Intent(getActivity(),
								DisplayMeetingActivity.class);
						nextIntent.putExtra("position", position);
//						nextIntent.putExtra("selected_meeting",
//								meetingList.get(position));
						startActivity(nextIntent);
					}
				});
			}
		} else {
			System.out.println("%%%% UserVisible false");

		}
	}

}