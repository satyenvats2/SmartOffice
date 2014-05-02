package com.mw.smartoff.DAO;

import java.util.List;

import android.content.Context;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserDAO {

	ParseQuery<ParseUser> query;

	public UserDAO(Context context) {
		super();
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo", "sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseUser.getQuery();
	}

	public ParseUser loginUser(String userName, String password) {
        try {
            ParseUser.logIn(userName, password);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return ParseUser.getCurrentUser();
	}
	
	public List<ParseUser> getAllUsers() {
		System.out.println("get all users");
		List<ParseUser> userList = null;
		try {
			userList = query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return userList;
	}

    public ParseUser getUserById(String userId) {
        System.out.println(">>>>>> get user by id");
        ParseUser contactUser = null;
        query.whereEqualTo("objectId", userId);
        try {
            contactUser = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return contactUser;
    }
}
