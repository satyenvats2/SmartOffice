package com.mw.smartoff.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

//import android.app.Fragment;

public class MeetingFragment extends Fragment {
	private static final String[] CONTENT = new String[] { "All Meetings", "New Meetings",
			"My Meetings" };

	TextView notifyMeetingTV;

	GlobalVariable globalVariable;
	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initThings();
//		FetchMeetingsAsynTask asynTask = new FetchMeetingsAsynTask();
//		asynTask.execute(new String[] { "Hello World" });

		View rootView = inflater.inflate(R.layout.meeting_list_fragment,
				container, false);
		return rootView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getActivity()
				.getSupportFragmentManager());

		ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) getActivity()
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new MeetingDAO(getActivity());
		dao2 = new ResponseToMeetingDAO(getActivity());
	}

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

	//
	private class FetchMeetingsAsynTask extends
			AsyncTask<String, Void, List<Meeting>> {

		@Override
		protected List<Meeting> doInBackground(String... params) {
			List<Meeting> meetingList = new ArrayList<Meeting>();
			List<ParseObject> meetingsPOList = dao
					.getMeetingsForUser(ParseUser.getCurrentUser().getEmail());

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
			return meetingList;
		}

		@Override
		protected void onPostExecute(final List<Meeting> meetingList) {
			super.onPostExecute(meetingList);
			
			FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getActivity()
					.getSupportFragmentManager());

			ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
			pager.setAdapter(adapter);

			TabPageIndicator indicator = (TabPageIndicator) getActivity()
					.findViewById(R.id.indicator);
			indicator.setViewPager(pager);

		}// onPostExec

	}// Asyn
	
	class GoogleMusicAdapter extends FragmentStatePagerAdapter {
		public GoogleMusicAdapter(
				android.support.v4.app.FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new TestFragment();
			case 1:
				return new TestFragment2();
			case 2:
				return new TestFragment3();
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

}
