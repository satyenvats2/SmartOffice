package com.mw.smartoff.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mw.smartoff.model.Email;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.model.Message;
import com.mw.smartoff.model.User;
import com.mw.smartoffice.R;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class GlobalVariable extends android.app.Application {

	public static int PIN = 0;
	public static boolean FROM_VERIFY_PIN;

	public static int TYPE_NOTIFICATION = -1;

	public static boolean RESPONDED_TO_MEETING;
	public static boolean RESPONSE_TO_MEETING;

	public static int MEETINGS_ALL = 1;
	public static int MEETINGS_PENDING = 10;
	public static int MEETINGS_MY = 100;

	List<Meeting> meetingList;
	List<Meeting> meetingPendingList;
	List<Meeting> meetingOwnList;

	List<Email> emailList;
	List<ParseUser> userList;

	HashMap<String, Integer> myMap;

	public GlobalVariable() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		String[] alphabets = getResources().getStringArray(R.array.alphabets);
		int[] hexCodes = getResources().getIntArray(R.array.hex_codes);
		myMap = new HashMap<String, Integer>();
		for (int i = 0; i < alphabets.length; i++) {
			myMap.put(alphabets[i], hexCodes[i]);
		}

		if (myMap == null)
			System.out.println("map null");
		else
			System.out.println("map not null");
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

	public List<Meeting> getMeetingPendingList() {
		return meetingPendingList;
	}

	public void setMeetingPendingList(List<Meeting> meetingPendingList) {
		this.meetingPendingList = meetingPendingList;
	}

	public List<ParseUser> getUserList() {
		return userList;
	}

	public void setUserList(List<ParseUser> userList) {
		this.userList = userList;
	}

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

	public HashMap<String, Integer> getMyMap() {
		return myMap;
	}

	public void setMyMap(HashMap<String, Integer> myMap) {
		this.myMap = myMap;
	}

	public Email convertPOtoEmail(ParseObject emailPO) {
//		ParseUser parseUser = emailPO.getParseUser("from");
//		System.out.println("sender's email   :  " + parseUser.getEmail());
		return new Email(emailPO.getObjectId(),
				convertParseObjectToUser(emailPO.getParseUser("from")),
				emailPO.getString("subject"), emailPO.getString("content"),
				emailPO.getBoolean("isMailRead"), emailPO.getCreatedAt());
	}

	public Meeting convertPOtoMeeting(ParseObject meetingPO) {
		return new Meeting(meetingPO.getObjectId(),
				convertParseObjectToUser(meetingPO.getParseUser("from")),
				meetingPO.getString("subject"),
				meetingPO.getString("description"),
				meetingPO.getString("location"), meetingPO.getDate("startTime"));
	}

	public User convertParseObjectToUser(ParseUser userPO) {
		return new User(userPO.getEmail(), userPO.getUsername(),
				userPO.getString("name"));
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

	public boolean haveNetworkConnection() {

		System.out.println("internet check");
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public static boolean hasActiveInternetConnection() {
		HttpURLConnection urlc;
		try {
			urlc = (HttpURLConnection) (new URL("https://www.google.co.in/")
					.openConnection());// http://www.google.com
			urlc.setRequestProperty("User-Agent", "Test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1500);
			urlc.connect();
			return (urlc.getResponseCode() == 200);
		} catch (MalformedURLException e) {
			// e.printStackTrace();
			System.out.println("except");
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("except");
		}

		return false;
	}

	Integer i;

	public int combinedInternetTest() {
		/*
		 * We can't return AlertDialog.Builder from this function because it
		 * requires context as argument & we do not have context available in
		 * this class
		 */
		if (!haveNetworkConnection()) {
			System.out.println("network problem");
			return 1;
		} else {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					if (!hasActiveInternetConnection())
						i = Integer.valueOf(5);
				}
			});
			t.start();

			while (!(t.getState() == Thread.State.TERMINATED)) {
				try {
					Thread.currentThread();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (i != null) {
				System.out.println("internet problem");
				return 2;
			}
		}
		return 0;
	}

	public String getDisplayDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd MMM, hh:mm");
		String stringDate = simpleDateFormat.format(date);
		return stringDate;
	}

	public static void resetOnLogout() {
		PIN = 0;
		FROM_VERIFY_PIN = false;
		RESPONDED_TO_MEETING = false;
		RESPONSE_TO_MEETING = false;
	}
}
