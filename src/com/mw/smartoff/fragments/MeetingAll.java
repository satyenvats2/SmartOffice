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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.MeetingService;
import com.mw.smartoffice.R;

public class MeetingAll extends Fragment {
	PullAndLoadListView meetingLV;
	TextView notificationTV;
	RelativeLayout progressBar_RL;

	GlobalVariable globalVariable;
	List<Meeting> meetingList;
	MeetingsAdapter adapter;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	Intent nextIntent;
	Intent serviceIntent;

	private BroadcastReceiver meetingReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			progressBar_RL.setVisibility(View.GONE);
			meetingList = globalVariable.getMeetingList();
			if (meetingList.size() < 1) {
				notificationTV.setVisibility(View.VISIBLE);
			}
			adapter.swapData(meetingList);
			meetingLV.onRefreshComplete();
			adapter.notifyDataSetChanged();
			Toast.makeText(context, "TestFrag1 broad response",
					Toast.LENGTH_SHORT).show();

		}
	};

	private void findThings() {
		meetingLV = (PullAndLoadListView) getActivity().findViewById(
				R.id.meeting_LV);
		notificationTV = (TextView) getActivity().findViewById(
				R.id.notification_TV);
		progressBar_RL = (RelativeLayout) getActivity()
				.findViewById(R.id.progressBar_RL);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

		serviceIntent = new Intent(getActivity(), MeetingService.class);

		meetingList = globalVariable.getMeetingList();
		if (meetingList.size() > 0) {
			progressBar_RL.setVisibility(View.GONE);
		}
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
		View rootView = inflater.inflate(R.layout.meeting_all, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();

		meetingLV
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						// Do work to refresh the list here.
						// new PullToRefreshDataTask().execute();
						getActivity().startService(serviceIntent);
					}
				});

		getActivity().startService(serviceIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
		// new FetchMeetingsAsynTask().execute(true);
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