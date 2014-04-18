package com.mw.smartoff.DAO;

import android.content.Context;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserDAO {

	ParseQuery<ParseObject> query;

	public UserDAO(Context context) {
		super();
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo", "sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("User");
	}

	boolean found = false;
	public ParseUser loginUser(String userName, String password) {
		ParseUser.logInInBackground(userName, password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      // Hooray! The user is logged in.
			    	found = true;
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    	e.printStackTrace();
			    }
			  }
			});
		return ParseUser.getCurrentUser();
	}
}
