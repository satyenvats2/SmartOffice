package com.mw.smartoff.services;

import java.util.List;

import android.app.Application;

import com.mw.smartoff.model.Email;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.model.User;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class GlobalVariable extends Application {

//	ParseObject userPO;
//	User user;

	List<Meeting> meetingList;
	List<Meeting> meetingOwnList;
	
	public List<Meeting> getMeetingOwnList() {
		return meetingOwnList;
	}

	public void setMeetingOwnList(List<Meeting> meetingOwnList) {
		this.meetingOwnList = meetingOwnList;
	}

	public List<Meeting> getMeetingList() {
		return meetingList;
	}

	public void setMeetingList(List<Meeting> meetingList) {
		this.meetingList = meetingList;
	}

//	public ParseObject getUserPO() {
//		return userPO;
//	}
//
//	public void setUserPO(ParseObject userPO) {
//		this.userPO = userPO;
//	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public Email convertPOtoEmail(ParseObject emailPO) {

		ParseUser dsadsadsa = emailPO.getParseUser("from");
		System.out.println("sender'd email   :  " + dsadsadsa.getEmail());
		return new Email(emailPO.getObjectId(),
				convertParseObjectToUser(emailPO.getParseUser("from")),
				emailPO.getString("subject"), emailPO.getString("content"),
				emailPO.getBoolean("isMailRead"), emailPO.getCreatedAt());
	}

	public User convertParseObjectToUser(ParseUser userPO) {
		return new User(userPO.getEmail(), userPO.getUsername());

	}

	public Meeting convertPOtoMeeting(ParseObject meetingPO) {
		return new Meeting(meetingPO.getObjectId(),convertParseObjectToUser(meetingPO.getParseUser("from")),
				meetingPO.getString("subject"),
				meetingPO.getString("description"),
				meetingPO.getString("location"), meetingPO.getDate("startTime"));
	}
}
