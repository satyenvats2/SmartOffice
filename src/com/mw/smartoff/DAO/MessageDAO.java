package com.mw.smartoff.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.parse.*;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageDAO {

	ParseQuery<ParseObject> query;
	ParseQuery<ParseObject> query2;

	public MessageDAO(Context context) {
		super();
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("Messages");
		query2 = ParseQuery.getQuery("Messages");

	}

	public List<ParseObject> getMsgsForUser(ParseUser fromPU, ParseUser toPU) {
		List<ParseObject> msgsList = null;
		query.whereEqualTo("fromUser", fromPU);
		query.whereEqualTo("toUser", toPU);
		query2.whereEqualTo("fromUser", toPU);
		query2.whereEqualTo("toUser", fromPU);

		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		queries.add(query);
		queries.add(query2);

		ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
		mainQuery.orderByAscending("createdAt");
		mainQuery.include("fromUser");
		mainQuery.include("toUser");
		try {
			msgsList = mainQuery.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return msgsList;
	}
	
	// SmartOffice
	public void saveMessage(ParseUser fromPU, ParseUser toPU, String message) {
		System.out.println("message is  : " + message);
		ParseObject parseObject = new ParseObject("Messages");
		parseObject.put("fromUser", fromPU);
		parseObject.put("toUser", toPU);
		parseObject.put("messageText", message);
		
		parseObject.saveEventually();

        JSONObject data = new JSONObject();
        try {
        	// this is the String receiver will recognise
            data.put("action", "com.mw.smartoff.STATUS_UPDATE");
            data.put("type", 2);
            data.put("alert", "asdf");
            data.put("fromUserId", fromPU.getObjectId());
            data.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(toPU.getUsername()); // Notice we use setChannels not setChannel
        push.setData(data);
        push.sendInBackground();

        // comment to be removed. for github.
	}
}
