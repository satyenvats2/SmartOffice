package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
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
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class TestFragment extends Fragment {
	ListView meetingLV;
	TextView notifyMeetingTV;

	GlobalVariable globalVariable;

	Intent nextIntent;
	MeetingsAdapter adapter;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	private void findThings() {
		meetingLV = (ListView) getActivity().findViewById(R.id.meeting_LV);
		notifyMeetingTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		createDialog = new CreateDialog(getActivity());
		progressDialog = createDialog.createProgressDialog("Loading11",
				"Fetching Meetings", true, null);
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

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
		System.out.println("frag1");
		findThings();
		initThings();
		progressDialog.show();
		FetchMeetingsAsynTask asynTask = new FetchMeetingsAsynTask();
		asynTask.execute(new String[] { "Hello World" });

	}

	private class FetchMeetingsAsynTask extends
			AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			List<Meeting> meetingList = new ArrayList<Meeting>();
			List<ParseObject> meetingsPOList = dao.getMeetingsForUser(ParseUser
					.getCurrentUser().getEmail());

			// make a list of meetings including response
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
			}// for()
			globalVariable.setMeetingList(meetingList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			final List<Meeting> meetingList = globalVariable.getMeetingList();
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
						nextIntent.putExtra("position", position);
						startActivity(nextIntent);
					}
				});
			}
			progressDialog.dismiss();
		}// onPostExec

	}// Asyn
}
