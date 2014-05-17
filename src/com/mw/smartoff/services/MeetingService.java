package com.mw.smartoff.services;

import java.util.ArrayList;
import java.util.List;

import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.model.Meeting;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class MeetingService extends IntentService {

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;
	GlobalVariable globalVariable;

	public MeetingService() {
		super("MeetingService");
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent nextIntent = new Intent("new_meeting");
		LocalBroadcastManager.getInstance(this)
				.sendBroadcast(nextIntent);
	
	}

}
