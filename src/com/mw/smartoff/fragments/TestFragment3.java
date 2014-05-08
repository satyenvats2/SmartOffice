package com.mw.smartoff.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TestFragment3 extends Fragment {
	PullAndLoadListView meetingLV;
	TextView notifyMeetingTV;
	ProgressBar progressBar;

	GlobalVariable globalVariable;

	Intent nextIntent;
	MeetingsAdapter adapter;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	private void findThings() {
		meetingLV = (PullAndLoadListView) getActivity().findViewById(
				R.id.meeting_LV3);
		notifyMeetingTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV);
		progressBar = (ProgressBar) getActivity().findViewById(
				R.id.progressBar3);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.test_fragment3, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();
		FetchMeetingsOwnAsynTask asynTask = new FetchMeetingsOwnAsynTask();
		asynTask.execute(true);

		meetingLV
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						// Do work to refresh the list here.
						// new PullToRefreshDataTask().execute();
						new FetchMeetingsOwnAsynTask().execute(false);
					}
				});
	}

	private class FetchMeetingsOwnAsynTask extends AsyncTask<Boolean, Void, Void> {

		@Override
		protected Void doInBackground(Boolean... params) {

			if (globalVariable.getMeetingOwnList() != null && params[0])
				return null;

			List<Meeting> meetingList = new ArrayList<Meeting>();
			List<ParseObject> meetingsPOList = dao
					.getOwnMeetingsForUser(ParseUser.getCurrentUser());

			System.out.println("size is  :  " + meetingsPOList.size());
			// make a list of meetings including response
			for (int i = 0; i < meetingsPOList.size(); i++) {
				meetingsPOList.get(i).put("from", ParseUser.getCurrentUser());
				ParseObject tempMeetingPO = meetingsPOList.get(i);
				Meeting tempMeeting = globalVariable
						.convertPOtoMeeting(tempMeetingPO);
				List<ParseObject> allResponsesPO = dao2
						.getAllResponsesForMeeting(tempMeetingPO);
				if (allResponsesPO != null) {
					tempMeeting.setResponses(allResponsesPO);
					System.out.println("not nullla");
				}
				else
					System.out.println("nullla");

				meetingList.add(tempMeeting);
			}// for()
			globalVariable.setMeetingOwnList(meetingList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			progressBar.setVisibility(View.INVISIBLE);

			final List<Meeting> meetingList = globalVariable
					.getMeetingOwnList();
			if (meetingList.size() == 0) {
				notifyMeetingTV.setText("No meetings found");
				notifyMeetingTV.setVisibility(View.VISIBLE);
			} else {
                meetingLV.onRefreshComplete();
				adapter = new MeetingsAdapter(getActivity(), meetingList);
				meetingLV.setAdapter(adapter);

				meetingLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						nextIntent = new Intent(getActivity(),
								DisplayMeetingActivity.class);
						nextIntent.putExtra("position", position-1);
						nextIntent.putExtra("type", GlobalVariable.MEETINGS_MY);
						startActivity(nextIntent);
					}
				});
			}
		}// onPostExec

	}// Asyn
}
