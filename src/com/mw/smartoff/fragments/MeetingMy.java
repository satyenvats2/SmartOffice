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
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.MeetingOwnService;
import com.mw.smartoffice.R;

public class MeetingMy extends Fragment {
	PullAndLoadListView meetingLV;
	TextView notificationTV;
	ProgressBar progressBar;

	GlobalVariable globalVariable;
	List<Meeting> meetingList;
	MeetingsAdapter adapter;

	Intent nextIntent;
	Intent serviceIntent;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	private BroadcastReceiver meetingOwnReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			progressBar.setVisibility(View.GONE);
			meetingList = globalVariable.getMeetingOwnList();
			if (meetingList.size() < 1) {
				notificationTV.setVisibility(View.VISIBLE);
			}
			adapter.swapData(meetingList);
			meetingLV.onRefreshComplete();
			adapter.notifyDataSetChanged();
//			Toast.makeText(context, "MeetingMyFrag broadcast response", Toast.LENGTH_SHORT).show();
		}
	};

	private void findThings() {
		meetingLV = (PullAndLoadListView) getActivity().findViewById(
				R.id.meeting_LV3);
		notificationTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV3);
		progressBar = (ProgressBar) getActivity().findViewById(
				R.id.progressBar3);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

		serviceIntent = new Intent(getActivity(), MeetingOwnService.class);

		meetingList = globalVariable.getMeetingOwnList();
		if (meetingList.size() > 0) {
			progressBar.setVisibility(View.GONE);
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
				nextIntent.putExtra("type", GlobalVariable.MEETINGS_MY);
				startActivity(nextIntent);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.meeting_my, container,
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