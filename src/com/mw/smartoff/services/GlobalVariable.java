package com.mw.smartoff.services;

import java.util.List;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

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

	List<Email> emailList;
	
	public List<Email> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<Email> emailList) {
		this.emailList = emailList;
	}

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
	
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 150;
		int targetHeight = 150;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}
}
