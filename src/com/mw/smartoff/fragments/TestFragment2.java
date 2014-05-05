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
import android.widget.Toast;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.DisplayMeetingActivity;
import com.mw.smartoff.adapter.MeetingsAdapter;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;

import java.util.ArrayList;
import java.util.List;

public class TestFragment2 extends Fragment {
	PullAndLoadListView meetingLV;
	TextView notifyMeetingTV;

	GlobalVariable globalVariable;

	Intent nextIntent;
	MeetingsAdapter adapter;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	List<Meeting> meetingList;// = new ArrayList<Meeting>();
	List<Meeting> tempMeetingList;

	ProgressBar progressBar;

	private void findThings() {
		meetingLV = (PullAndLoadListView) getActivity().findViewById(R.id.meeting_LV2);
		notifyMeetingTV = (TextView) getActivity().findViewById(
				R.id.notify_meeting_TV);
		progressBar = (ProgressBar) getActivity().findViewById(
				R.id.progressBar2);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.test_fragment2, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Toast.makeText(getActivity(), "onViewCreated2", Toast.LENGTH_SHORT)
				.show();

		findThings();
		initThings();
		
		meetingLV
		.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// Do work to refresh the list here.
				// new PullToRefreshDataTask().execute();
				new SortMeetingsAsynTask().execute(false);
			}
		});
	}

    public void onResume()
    {
        super.onResume();
        SortMeetingsAsynTask asynTask = new SortMeetingsAsynTask();
        asynTask.execute(true);

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		meetingList = new ArrayList<Meeting>();
//		if (isVisibleToUser) {
////			Toast.makeText(getActivity(), "%%%% UserVisible true",
////					Toast.LENGTH_SHORT).show();
//			System.out.println("%%%% UserVisible true");
//
//			SortMeetingsAsynTask asynTask = new SortMeetingsAsynTask();
//			asynTask.execute(true);
//			tempMeetingList = globalVariable.getMeetingList();
//			for (int i = 0; i < tempMeetingList.size(); i++) {
//				if (!tempMeetingList.get(i).isHasBeenResponsedTo())
//					meetingList.add(tempMeetingList.get(i));
//			}
//			if (meetingList.size() == 0) {
//				notifyMeetingTV.setText("No meetings found");
//				notifyMeetingTV.setVisibility(View.VISIBLE);
//			} else {
//				Toast.makeText(getActivity(), "else" + meetingList.size(),
//						Toast.LENGTH_SHORT).show();
//				adapter = new MeetingsAdapter(getActivity(), meetingList);
//				meetingLV.setAdapter(adapter);
//
//				meetingLV.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View v,
//							int position, long id) {
//						System.out.println("position  :  " + position);
//						nextIntent = new Intent(getActivity(),
//								DisplayMeetingActivity.class);
//						nextIntent.putExtra("position", position);
//						// nextIntent.putExtra("selected_meeting",
//						// meetingList.get(position));
//						startActivity(nextIntent);
//					}
//				});
//			}
//		} else {
//			System.out.println("%%%% UserVisible false");
//
//		}
//	}

	private class SortMeetingsAsynTask extends AsyncTask<Boolean, Void, Void> {

		@Override
		protected Void doInBackground(Boolean... params) {

			if (globalVariable.getMeetingPendingList() != null && params[0])
				return null;

			meetingList = new ArrayList<Meeting>();
			tempMeetingList = globalVariable.getMeetingList();
			for (int i = 0; i < tempMeetingList.size(); i++) {
				if (!tempMeetingList.get(i).isHasBeenResponsedTo())
					meetingList.add(tempMeetingList.get(i));
			}

			globalVariable.setMeetingPendingList(meetingList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.INVISIBLE);
			final List<Meeting> meetingList = globalVariable
					.getMeetingPendingList();
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
						System.out.println("position  :  " + position);
						nextIntent = new Intent(getActivity(),
								DisplayMeetingActivity.class);
						nextIntent.putExtra("position", position-1);
						nextIntent.putExtra("type", GlobalVariable.MEETINGS_PENDING);
						startActivity(nextIntent);
					}
				});
			}

		}// onPostExec

	}// Asyn
}