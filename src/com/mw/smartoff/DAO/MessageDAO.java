package com.mw.smartoff.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
		mainQuery.orderByDescending("createdAt");
		mainQuery.include("fromUser");
		mainQuery.include("toUser");
		try {
			msgsList = mainQuery.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return msgsList;
	}
	
	public void saveMsgs(ParseUser fromPU, ParseUser toPU, String message) {
		ParseObject parseObject = new ParseObject("Messages");
		parseObject.put("fromUser", fromPU);
		parseObject.put("toUser", toPU);
		parseObject.put("messageText", message);
		
		parseObject.saveEventually();
	}
}
