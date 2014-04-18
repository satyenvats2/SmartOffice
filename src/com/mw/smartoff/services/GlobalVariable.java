package com.mw.smartoff.services;

import android.app.Application;

import com.mw.smartoff.model.User;
import com.parse.ParseObject;

public class GlobalVariable extends Application {

	ParseObject userPO;
	User user;

	public ParseObject getUserPO() {
		return userPO;
	}

	public void setUserPO(ParseObject userPO) {
		this.userPO = userPO;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
