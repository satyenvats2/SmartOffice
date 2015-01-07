package com.mw.smartoff.extras;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
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

	// Used in DisplayMeetingActivity.java
	public static int MEETINGS_ALL = 1;
	public static int MEETINGS_PENDING = 10;
	public static int MEETINGS_MY = 100;

	List<Email> emailList;

	List<Meeting> meetingList;
	List<Meeting> meetingPendingList;
	List<Meeting> meetingOwnList;

	List<ParseUser> userList;

	List<ParseObject> attendancePOList;

	ParseUser chatPerson;

	HashMap<String, Integer> myMap;

	public static final String TAG = GlobalVariable.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static GlobalVariable myAppInstance;

	public GlobalVariable() {
		emailList = new ArrayList<Email>();
		meetingList = new ArrayList<Meeting>();
		meetingOwnList = new ArrayList<Meeting>();
		attendancePOList = new ArrayList<ParseObject>();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println(">>>>>>>>>>> GV onCreate");
		myAppInstance = this;
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

	public List<Meeting> getMeetingList() {
		return meetingList;
	}

	public void setMeetingList(List<Meeting> meetingList) {
		this.meetingList = meetingList;
	}

	public List<Meeting> getMeetingPendingList() {
		return meetingPendingList;
	}

	public void setMeetingPendingList(List<Meeting> meetingPendingList) {
		this.meetingPendingList = meetingPendingList;
	}

	public List<Meeting> getMeetingOwnList() {
		return meetingOwnList;
	}

	public List<ParseObject> getAttendancePOList() {
		return attendancePOList;
	}

	public void setAttendancePOList(List<ParseObject> attendancePOList) {
		this.attendancePOList = attendancePOList;
	}

	public void setMeetingOwnList(List<Meeting> meetingOwnList) {
		this.meetingOwnList = meetingOwnList;
	}

	public List<Email> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<Email> emailList) {
		this.emailList = emailList;
	}

	public List<ParseUser> getUserList() {
		return userList;
	}

	public void setUserList(List<ParseUser> userList) {
		this.userList = userList;
	}

	public ParseUser getChatPerson() {
		return chatPerson;
	}

	public void setChatPerson(ParseUser chatPerson) {
		this.chatPerson = chatPerson;
	}

	public HashMap<String, Integer> getMyMap() {
		return myMap;
	}

	public void setMyMap(HashMap<String, Integer> myMap) {
		this.myMap = myMap;
	}

	public Email convertPOtoEmail(ParseObject emailPO) {
		// ParseUser parseUser = emailPO.getParseUser("from");
		// System.out.println("sender's email   :  " + parseUser.getEmail());
		return new Email(emailPO.getObjectId(),
				convertParseUserToUser(emailPO.getParseUser("from")),
				emailPO.getString("subject"), emailPO.getString("content"),
				emailPO.getBoolean("isMailRead"), emailPO.getCreatedAt());
	}

	public Meeting convertPOtoMeeting(ParseObject meetingPO) {
		return new Meeting(meetingPO.getObjectId(),
				convertParseUserToUser(meetingPO.getParseUser("from")),
				meetingPO.getString("subject"),
				meetingPO.getString("description"),
				meetingPO.getString("location"), meetingPO.getDate("startTime"));
	}

	public List<User> convertPUListToUserList(List<ParseUser> userListPU) {
		List<User> aa = new ArrayList<User>();
		for (int i = 0; i < userListPU.size(); i++) {
			aa.add(convertParseUserToUser(userListPU.get(i)));
		}
		return aa;
	}

	public List<ParseUser> convertUserListToPuList(List<User> userList) {
		List<ParseUser> aa = new ArrayList<ParseUser>();
		for (int i = 0; i < userList.size(); i++) {
			aa.add(convertUserToParseUser(userList.get(i)));
		}
		return aa;
	}

	public User convertParseUserToUser(ParseUser userPO) {
		return new User(userPO.getObjectId(), userPO.getEmail(),
				userPO.getUsername(), userPO.getString("name"));
	}

	public ParseUser convertUserToParseUser(User userPO) {
		ParseUser aa = new ParseUser();
		aa.setObjectId(userPO.getObjectId());
		aa.setUsername(userPO.getUsername());
		aa.setEmail(userPO.getEmail());
		aa.put("name", userPO.getName());
		return aa;
	}

	public Message convertPOtoMessage(ParseObject meetingPO) {
		return new Message(meetingPO.getObjectId(),
				meetingPO.getParseUser("fromUser"),
				meetingPO.getParseUser("toUser"),
				meetingPO.getString("messageText"), meetingPO.getCreatedAt());
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

	public void resetOnLogout2() {
		setEmailList(new ArrayList<Email>());
		setMeetingList(new ArrayList<Meeting>());
		setMeetingOwnList(new ArrayList<Meeting>());
		setMeetingPendingList(new ArrayList<Meeting>());
		setUserList(new ArrayList<ParseUser>());
	}

	public Date addToDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	public static synchronized GlobalVariable getInstance() {
		return myAppInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
