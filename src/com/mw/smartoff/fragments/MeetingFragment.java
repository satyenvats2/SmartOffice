package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;

//import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MeetingFragment extends Fragment {

	ListView meetingLV;
	GlobalVariable globalVariable;
	MeetingDAO dao;
	ResponseToMeetingDAO dao2;
	TextView notifyMeetingTV;

	MeetingsAdapter adapter;
	Intent nextIntent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.meeting_list_fragment,
				container, false);
		return rootView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();
		myOwnListeners();
		FetchMeetingsAsynTask asynTask = new FetchMeetingsAsynTask();
		asynTask.execute(new String[] { "Hello World" });

	}

	private void findThings() {
		meetingLV = (ListView) getActivity().findViewById(R.id.meeting_LV);
		notifyMeetingTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());
	}

	private class FetchMeetingsAsynTask extends
			AsyncTask<String, Void, List<Meeting>> {
		// ParseUser user;
		@Override
		protected List<Meeting> doInBackground(String... params) {
			List<Meeting> meetingList = new ArrayList<Meeting>();
			List<ParseObject> meetingsPOList = dao
					.getMeetingsForUser(globalVariable.getUser().getEmail());

			for (int i = 0; i < meetingsPOList.size(); i++) {
				ParseObject tempMeetingPO = meetingsPOList.get(i);
				Meeting tempMeeting = globalVariable
						.convertPOtoMeeting(meetingsPOList.get(i));
				ParseObject checkResponsePO = dao2
						.getCurrentResponseForMeeting(
								ParseUser.getCurrentUser(), tempMeetingPO);
				if (checkResponsePO != null) {
					tempMeeting.setHasBeenResponsedTo(true);
					tempMeeting.setCurrentResponse(checkResponsePO
							.getBoolean("isAttending"));
				}
				meetingList.add(tempMeeting);
			}

			return meetingList;
		}

		@Override
		protected void onPostExecute(List<Meeting> meetingsList) {
			super.onPostExecute(meetingsList);
			if (meetingsList.size() == 0) {
				notifyMeetingTV.setText("No meetings found");
				notifyMeetingTV.setVisibility(View.VISIBLE);
			} else {

				adapter = new MeetingsAdapter(getActivity(), meetingsList);
				meetingLV.setAdapter(adapter);

				meetingLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						nextIntent = new Intent(getActivity(),
								DisplayMeetingActivity.class);
						startActivity(nextIntent);
					}
				});
			}
		}// onPostExec

	}// Asyn

	private Meeting convertPOtoMeeting(ParseObject meetingPO) {
		return new Meeting(meetingPO.getString("subject"),
				meetingPO.getString("description"),
				meetingPO.getString("location"), meetingPO.getDate("startTime"));
	}

	private void myOwnListeners() {

		// acceptMeeting.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Toast.makeText(getActivity(), "Put accept functionality",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

	}
}
