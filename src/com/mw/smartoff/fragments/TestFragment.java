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

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoff.services.MeetingService;
import com.mw.smartoffice.R;

public class TestFragment extends Fragment {
	PullAndLoadListView meetingLV;
	// TextView notifyMeetingTV;
	ProgressBar progressBar;

	GlobalVariable globalVariable;
	List<Meeting> meetingList;
//	List<Meeting> tempList;
	Intent nextIntent;
	MeetingsAdapter adapter;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	Intent serviceIntent;

	private BroadcastReceiver meetingReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			meetingList = globalVariable.getMeetingList();
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
				R.id.meeting_LV);
		// notifyMeetingTV = (TextView) getActivity().findViewById(
		// R.id.notify_meeting_TV);
		progressBar = (ProgressBar) getActivity()
				.findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

		serviceIntent = new Intent(getActivity(), MeetingService.class);

		meetingList = globalVariable.getMeetingList();
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
		View rootView = inflater.inflate(R.layout.test_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();
		// FetchMeetingsAsynTask asynTask = new FetchMeetingsAsynTask();
		// asynTask.execute(true);

		meetingLV
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						// Do work to refresh the list here.
						// new PullToRefreshDataTask().execute();
						// new FetchMeetingsAsynTask().execute(false);
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
				meetingReceiver, new IntentFilter("new_meeting"));
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				meetingReceiver);
	}

}
//private class FetchMeetingsAsynTask extends AsyncTask<Boolean, Void, Void> {
//
//	@Override
//	protected Void doInBackground(Boolean... params) {
//
//		 if (globalVariable.getMeetingList() != null && params[0])
//		 return null;
//
//		 List<Meeting> meetingList = new ArrayList<Meeting>();
//		 List<ParseObject> meetingsPOList =
//		 dao.getMeetingsForUser(ParseUser
//		 .getCurrentUser().getEmail());
//		
//		 // make a list of meetings including response
//		 for (int i = 0; i < meetingsPOList.size(); i++) {
//		 ParseObject tempMeetingPO = meetingsPOList.get(i);
//		 Meeting tempMeeting = globalVariable
//		 .convertPOtoMeeting(meetingsPOList.get(i));
//		 ParseObject checkResponsePO = dao2
//		 .getCurrentResponseForMeeting(
//		 ParseUser.getCurrentUser(), tempMeetingPO);
//		 if (checkResponsePO != null) {
//		 tempMeeting.setHasBeenResponsedTo(true);
//		 tempMeeting.setCurrentResponse(checkResponsePO
//		 .getBoolean("isAttending"));
//		 }
//		 meetingList.add(tempMeeting);
//		 }// for()
//		 globalVariable.setMeetingList(meetingList);
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(Void result) {
//		super.onPostExecute(result);
//		progressBar.setVisibility(View.INVISIBLE);
//		final List<Meeting> meetingList = globalVariable.getMeetingList();
//		if (meetingList.size() > 0) {
//			meetingLV.onRefreshComplete();
//			adapter = new MeetingsAdapter(getActivity(), meetingList);
//			meetingLV.setAdapter(adapter);
//
//			meetingLV.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View v,
//						int position, long id) {
//					System.out.println("position  :  " + position);
//					nextIntent = new Intent(getActivity(),
//							DisplayMeetingActivity.class);
//					nextIntent.putExtra("position", position - 1);
//					nextIntent
//							.putExtra("type", GlobalVariable.MEETINGS_ALL);
//					startActivity(nextIntent);
//				}
//			});
//		}
//	}// onPostExec
//
//}// Asyn
