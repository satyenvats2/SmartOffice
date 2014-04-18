package com.mw.smartoff.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class EmailDAO {

	ParseQuery<ParseObject> query;
	ParseQuery<ParseObject> query2;

	public EmailDAO(Context context) {
		super();
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("Emails");
		query2 = ParseQuery.getQuery("Emails");

	}

	public List<ParseObject> getEmailsForUser(String emailID) {
		System.out.println("email is : " + emailID);
		List<ParseObject> emailList = null;
		query.whereEqualTo("cc", emailID);
		query2.whereEqualTo("to", emailID);

		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		queries.add(query);
		queries.add(query2);

		ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
		mainQuery.orderByAscending("createdAt");
		try {
			emailList = mainQuery.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return emailList;
	}
}
