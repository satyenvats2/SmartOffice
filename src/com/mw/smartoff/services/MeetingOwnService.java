package com.mw.smartoff.services;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.model.Meeting;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MeetingOwnService extends IntentService {

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;
	GlobalVariable globalVariable;

	public MeetingOwnService() {
		super("MeetingOwnService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new MeetingDAO(this);
		dao2 = new ResponseToMeetingDAO(this);
		globalVariable = (GlobalVariable) this.getApplicationContext();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent nextIntent = new Intent("meeting_own");
		LocalBroadcastManager.getInstance(this)
				.sendBroadcast(nextIntent);
	
	}

}
