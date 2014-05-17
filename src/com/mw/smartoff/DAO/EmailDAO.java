package com.mw.smartoff.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class EmailDAO {

	ParseQuery<ParseObject> query;
	ParseQuery<ParseObject> query2;
	Context context;

	public EmailDAO(Context context) {
		super();
		this.context = context;
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
		mainQuery.orderByDescending("createdAt");
		mainQuery.include("from");
		try {
			emailList = mainQuery.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return emailList;
	}

	public void markEmailAsRead(String userId) {
		ParseObject emailPO = null;
		try {
			emailPO = query.get(userId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (emailPO != null) {
			emailPO.put("isMailRead", true);
			emailPO.saveEventually(new SaveCallback() {

				@Override
				public void done(ParseException arg0) {
					Toast.makeText(context, "Marked as read",
							Toast.LENGTH_SHORT).show();

				}
			});
		}
	}
}
