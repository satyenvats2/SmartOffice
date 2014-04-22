package com.mw.smartoff.DAO;

import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ResponseToMeetingDAO {

	ParseQuery<ParseObject> query;

	public ResponseToMeetingDAO(Context context) {
		super();
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("ResponseToMeeting");

	}

	public ParseObject getCurrentResponseForMeeting(ParseUser user,
			ParseObject meetingPO) {
		List<ParseObject> tempList = null;
		query.whereEqualTo("responseFrom", user);
		query.whereEqualTo("meeting", meetingPO);

		try {
			tempList = query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (tempList != null && tempList.size() > 0)
			return tempList.get(0);
		else
			return null;

	}

	public void repondToMeeting() {

	}
}
