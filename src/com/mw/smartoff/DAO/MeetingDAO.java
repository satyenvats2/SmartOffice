package com.mw.smartoff.DAO;

import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MeetingDAO {

	ParseQuery<ParseObject> query;
	ResponseToMeetingDAO dao;

	public MeetingDAO(Context context) {
		super();
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("Meetings");
		dao = new ResponseToMeetingDAO(context);
	}

	public List<ParseObject> getMeetingsForUser(String emailID) {
		System.out.println("email is : " + emailID);
		List<ParseObject> meetingList = null;
		query.whereEqualTo("to", emailID);
		query.orderByDescending("createdAt");
		query.include("from");
		try {
			meetingList = query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return meetingList;
	}

	public List<ParseObject> getOwnMeetingsForUser(ParseUser user) {
		List<ParseObject> meetingList = null;

		query.whereEqualTo("from", user);
		query.orderByAscending("createdAt");
		try {
			meetingList = query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (meetingList != null && meetingList.size() > 0)
			for (int i = 0; i < meetingList.size(); i++) {
				List<ParseObject> allResponsesPO = dao
						.getAllResponsesForMeeting(meetingList.get(i));
				if (allResponsesPO != null && allResponsesPO.size() > 0)
					meetingList.get(i).put("allResponses", allResponsesPO);
			}
		return meetingList;
	}

	public ParseObject getMeetingByID(String id) {
		System.out.println("meeting id is  :  " + id);
		ParseObject meetingList = null;
		try {
			meetingList = query.get(id);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (meetingList != null)
			return meetingList;
		else
			return null;

	}
}
