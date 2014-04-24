package com.mw.smartoff.fragments;

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

import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;

public class TestFragment extends Fragment {
	ListView meetingLV;
	TextView notifyMeetingTV;

	GlobalVariable globalVariable;

	List<Meeting> meetingList;
	Intent nextIntent;
	MeetingsAdapter adapter;

	private void findThings() {
		meetingLV = (ListView) getActivity().findViewById(R.id.meeting_LV);
		notifyMeetingTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		while(meetingList == null)
		meetingList = globalVariable.getMeetingList();
		// dao = new MeetingDAO(getActivity());
		// dao2 = new ResponseToMeetingDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.test_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();
		if (meetingList.size() == 0) {
			notifyMeetingTV.setText("No meetings found");
			notifyMeetingTV.setVisibility(View.VISIBLE);
		} else {

			adapter = new MeetingsAdapter(getActivity(), meetingList);
			meetingLV.setAdapter(adapter);

			meetingLV.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					nextIntent = new Intent(getActivity(),
							DisplayMeetingActivity.class);
					nextIntent.putExtra("selected_meeting",
							meetingList.get(position));
					startActivity(nextIntent);
				}
			});
		}
	}
}
