package com.mw.smartoff.services;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

import com.mw.smartoff.MainActivity;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.model.Message;
import com.mw.smartoff.model.User;
import com.parse.*;

import java.util.List;
import java.util.Set;

public class GlobalVariable extends android.app.Application {

	public GlobalVariable() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the Parse SDK.
		Parse.initialize(this, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");

		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);

		// TODO: move to correct place
		// Clear push subscription channels
		// Set<String> setOfAllSubscriptions =
		// PushService.getSubscriptions(this);
		// for (String setOfAllSubscription : setOfAllSubscriptions) {
		// System.out.println(">>>>>>> LoginActivity::onCreate() - " +
		// setOfAllSubscription);
		// PushService.unsubscribe(this, setOfAllSubscription);
		// }
		// Specify an Activity to handle all pushes by default.
		// PushService.setDefaultPushCallback(this, MainActivity.class);
		// if (ParseUser.getCurrentUser() != null) {
		// PushService.subscribe(this, "SmartOffice", MainActivity.class);
		// PushService.subscribe(this, ParseUser.getCurrentUser().getUsername(),
		// MainActivity.class);
		// } else {
		// PushService.subscribe(this, "SmartOffice", MainActivity.class);
		// }

	}

	List<Meeting> meetingList;
	List<Meeting> meetingOwnList;

	List<Email> emailList;

	public List<ParseUser> getUserList() {
		return userList;
	}

	public void setUserList(List<ParseUser> userList) {
		this.userList = userList;
	}

	List<ParseUser> userList;

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

	public Email convertPOtoEmail(ParseObject emailPO) {

		ParseUser parseUser = emailPO.getParseUser("from");
		System.out.println("sender's email   :  " + parseUser.getEmail());
		return new Email(emailPO.getObjectId(),
				convertParseObjectToUser(emailPO.getParseUser("from")),
				emailPO.getString("subject"), emailPO.getString("content"),
				emailPO.getBoolean("isMailRead"), emailPO.getCreatedAt());
	}

	public User convertParseObjectToUser(ParseUser userPO) {
		return new User(userPO.getEmail(), userPO.getUsername());

	}

	public Meeting convertPOtoMeeting(ParseObject meetingPO) {
		return new Meeting(meetingPO.getObjectId(),
				convertParseObjectToUser(meetingPO.getParseUser("from")),
				meetingPO.getString("subject"),
				meetingPO.getString("description"),
				meetingPO.getString("location"), meetingPO.getDate("startTime"));
	}

	public Message convertPOtoMessage(ParseObject meetingPO) {
		return new Message(meetingPO.getObjectId(),
				meetingPO.getParseUser("fromUser"),
				meetingPO.getParseUser("toUser"),
				meetingPO.getString("messageText"));
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
