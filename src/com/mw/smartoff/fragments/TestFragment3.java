package com.mw.smartoff.fragments;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoff.services.MeetingOwnService;
import com.mw.smartoffice.R;

public class TestFragment3 extends Fragment {
	PullAndLoadListView meetingLV;
	TextView notifyMeetingTV;
	ProgressBar progressBar;

	GlobalVariable globalVariable;
	List<Meeting> meetingList;
	
	Intent nextIntent;
	MeetingsAdapter adapter;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	Intent serviceIntent;
	
	private BroadcastReceiver meetingOwnReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			meetingList = globalVariable.getMeetingOwnList();
			adapter.swapData(meetingList);
			System.out.println("<><><><><><><>meetings size : "
					+ meetingList.size());
			meetingLV.onRefreshComplete();
			adapter.notifyDataSetChanged();
			System.out.println(">>>>><<<<<<<<suyccesese");

		}
	};
	
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
		
		serviceIntent = new Intent(getActivity(), MeetingOwnService.class);
		
		meetingList = globalVariable.getMeetingOwnList();
		adapter = new MeetingsAdapter(getActivity(), meetingList);
		meetingLV.setAdapter(adapter);

		meetingLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				System.out.println("position  :  " + position);
				nextIntent = new Intent(getActivity(),
						DisplayMeetingActivity.class);
				nextIntent.putExtra("position", position - 1);
				nextIntent.putExtra("type", GlobalVariable.MEETINGS_ALL);
				startActivity(nextIntent);
			}
		});
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
//		FetchMeetingsOwnAsynTask asynTask = new FetchMeetingsOwnAsynTask();
//		asynTask.execute(true);

		meetingLV
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						// Do work to refresh the list here.
						// new PullToRefreshDataTask().execute();
//						new FetchMeetingsOwnAsynTask().execute(false);
						getActivity().startService(serviceIntent);
					}
				});
		getActivity().startService(serviceIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
//		new FetchMeetingsAsynTask().execute(true);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				meetingOwnReceiver, new IntentFilter("new_meeting"));
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				meetingOwnReceiver);
	}
	
	
}
//	private class FetchMeetingsOwnAsynTask extends AsyncTask<Boolean, Void, Void> {
//
//		@Override
//		protected Void doInBackground(Boolean... params) {
//
//			if (globalVariable.getMeetingOwnList() != null && params[0])
//				return null;
//
//			List<Meeting> meetingList = new ArrayList<Meeting>();
//			List<ParseObject> meetingsPOList = dao
//					.getOwnMeetingsForUser(ParseUser.getCurrentUser());
//
//			System.out.println("size is  :  " + meetingsPOList.size());
//			// make a list of meetings including response
//			for (int i = 0; i < meetingsPOList.size(); i++) {
//				meetingsPOList.get(i).put("from", ParseUser.getCurrentUser());
//				ParseObject tempMeetingPO = meetingsPOList.get(i);
//				Meeting tempMeeting = globalVariable
//						.convertPOtoMeeting(tempMeetingPO);
//				List<ParseObject> allResponsesPO = dao2
//						.getAllResponsesForMeeting(tempMeetingPO);
//				if (allResponsesPO != null) {
//					tempMeeting.setResponses(allResponsesPO);
//					System.out.println("not nullla");
//				}
//				else
//					System.out.println("nullla");
//
//				meetingList.add(tempMeeting);
//			}// for()
//			globalVariable.setMeetingOwnList(meetingList);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//
//			progressBar.setVisibility(View.INVISIBLE);
//
//			final List<Meeting> meetingList = globalVariable
//					.getMeetingOwnList();
//			if (meetingList.size() == 0) {
//				notifyMeetingTV.setText("No meetings found");
//				notifyMeetingTV.setVisibility(View.VISIBLE);
//			} else {
//                meetingLV.onRefreshComplete();
//				adapter = new MeetingsAdapter(getActivity(), meetingList);
//				meetingLV.setAdapter(adapter);
//
//				meetingLV.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View v,
//							int position, long id) {
//						nextIntent = new Intent(getActivity(),
//								DisplayMeetingActivity.class);
//						nextIntent.putExtra("position", position-1);
//						nextIntent.putExtra("type", GlobalVariable.MEETINGS_MY);
//						startActivity(nextIntent);
//					}
//				});
//			}
//		}// onPostExec
//
//	}// Asyn

