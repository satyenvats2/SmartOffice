package com.mw.smartoff;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

public class LoginActivity extends Activity {

	EditText emailET;
	EditText passwordET;
	TextView errorMsgTV;

	ProgressBar progressBarPB;

	UserDAO userDAO;
	EmailDAO emailDAO;
	MeetingDAO meetingDAO;
	ResponseToMeetingDAO responseToMeetingDAO;

	GlobalVariable globalVariable;
	Intent nextIntent;

	SharedPreferences sharedPreferences;
	Editor editor;

	private void findThings() {
		emailET = (EditText) findViewById(R.id.email_ET);
		passwordET = (EditText) findViewById(R.id.password_ET);
		progressBarPB = (ProgressBar) findViewById(R.id.progressBar_PB);
		errorMsgTV = (TextView) findViewById(R.id.error_msg_TV);
	}

	private void myOwnListeners() {
		emailET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				emailET.setError(null);
			}
		});

		passwordET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				passwordET.setError(null);
			}
		});
	}

	private void initThings() {
		userDAO = new UserDAO(this);
		emailDAO = new EmailDAO(this);
		meetingDAO = new MeetingDAO(this);
		responseToMeetingDAO = new ResponseToMeetingDAO(this);

		globalVariable = (GlobalVariable) getApplicationContext();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		editor = sharedPreferences.edit();
		// nextIntent = new Intent(LoginActivity.this, MainActivity.class);

		passwordET
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							onLogin(null);
						}
						return false;
					}
				});

		nextIntent = new Intent(LoginActivity.this, MainActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_page);
		findThings();
		myOwnListeners();
		initThings();

		if (ParseUser.getCurrentUser() != null) {
			System.out
					.println(">>>>>>> LoginActivity::onCreate -> user is present in preferences");
			// collectUserData();

			// retrieve data from preferences
			Gson gson = new Gson();
			if (sharedPreferences.contains("email_list")) {
				String value = sharedPreferences.getString("email_list", null);
				if (value != null) {
					Type listType = (Type) new TypeToken<ArrayList<Email>>() {
                    }.getType();
					globalVariable.setEmailList((List<Email>)gson.fromJson(value, listType));
				}
			}
			if (sharedPreferences.contains("meeting_list")) {
				String value = sharedPreferences.getString("meeting_list", null);
				if (value != null) {
					Type listType = (Type) new TypeToken<ArrayList<Meeting>>() {
                    }.getType();
					globalVariable.setMeetingList((List<Meeting>)gson.fromJson(value, listType));
				}
			}
			if (sharedPreferences.contains("meeting_own_list")) {
				String value = sharedPreferences.getString("meeting_list", null);
				if (value != null) {
					Type listType = (Type) new TypeToken<ArrayList<Meeting>>() {
                    }.getType();
					globalVariable.setMeetingOwnList((List<Meeting>)gson.fromJson(value, listType));
				}
			}
			if (sharedPreferences.contains("meeting_pending_list")) {
				String value = sharedPreferences.getString("meeting_pending_list", null);
				if (value != null) {
					Type listType = (Type) new TypeToken<ArrayList<Meeting>>() {
                    }.getType();
					globalVariable.setMeetingPendingList((List<Meeting>)gson.fromJson(value, listType));
				}
			}
			if (sharedPreferences.contains("user_list")) {
				String value = sharedPreferences.getString("user_list", null);
				if (value != null) {
					Type listType = (Type) new TypeToken<ArrayList<Email>>() {
                    }.getType();
					globalVariable.setUserList((List<ParseUser>)gson.fromJson(value, listType));
				}
			}
//			else
//			{
//				System.out.println("elsing");
//				Toast.makeText(this, "elseing", Toast.LENGTH_SHORT).show();
//			}

			startActivity(nextIntent);
		}

		((RelativeLayout) findViewById(R.id.login_page_RL))
				.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), 0);
						return false;
					}
				});

	}

	public void onLogin(View view) {
		if (!validate())
			return;
		progressBarPB.setVisibility(View.VISIBLE);
		LoginUserAsynTask asynTask = new LoginUserAsynTask();
		asynTask.execute(new String[] { "hello world" });
	}

	private boolean validate() {
		boolean bool = true;
		if (emailET.getText().toString().trim().length() == 0) {
			emailET.setError("Enter a user name");
			bool = false;
		}
		if (emailET.getText().length() == 0) {
			passwordET.setError("Enter a password");
			bool = false;
		}
		return bool;
	}

	private class LoginUserAsynTask extends AsyncTask<String, Void, ParseUser> {
		// ParseUser user;

		@SuppressLint("DefaultLocale")
		@Override
		protected ParseUser doInBackground(String... params) {
			ParseUser user = userDAO.loginUser(emailET.getText().toString()
					.trim().toLowerCase(), passwordET.getText().toString());
			return user;
		}

		@Override
		protected void onPostExecute(ParseUser user) {
			super.onPostExecute(user);
			progressBarPB.setVisibility(View.INVISIBLE);
			if (user != null) {
				System.out
						.println(">>>>>>> LoginActivity::onPostCreate() - user is "
								+ user.getUsername());
				// collectUserData();
				PushService.subscribe(LoginActivity.this, ParseUser
						.getCurrentUser().getUsername(), MainActivity.class);
				startActivity(nextIntent);
			} else {
				System.out
						.println(">>>>>>> LoginActivity::onPostCreate() - user is null");
				errorMsgTV.setText("Incorrect username or password");
				errorMsgTV.setVisibility(View.VISIBLE);
			}
		}

	}

	private void collectUserData() {
		// FetchEmailsAsynTask asynTask = new FetchEmailsAsynTask();
		// asynTask.execute(new String[] { "Hello Worlkd" });

		// FetchMeetingsAsynTask asynTask2 = new FetchMeetingsAsynTask();
		// asynTask2.execute(new String[] { "Hello Worlkd" });

		// FetchMeetingsOwnAsynTask asynTask3 = new FetchMeetingsOwnAsynTask();
		// asynTask3.execute(true);

		// FetchAllUsersAsynTask asynTask4 = new FetchAllUsersAsynTask();
		// asynTask4.execute(true);
	}

	private class FetchEmailsAsynTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {

			List<Email> emailList = new ArrayList<Email>();
			List<ParseObject> emailPOList = emailDAO.getEmailsForUser(ParseUser
					.getCurrentUser().getEmail());
			if (emailPOList != null) {
				for (int i = 0; i < emailPOList.size(); i++) {
					ParseObject tempEmailPO = emailPOList.get(i);
					Email tempMeeting = globalVariable
							.convertPOtoEmail(tempEmailPO);
					emailList.add(tempMeeting);
				}
				globalVariable.setEmailList(emailList);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}// onPostExecute()

	}// Asyn

	private class FetchMeetingsAsynTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			List<Meeting> meetingList = new ArrayList<Meeting>();
			List<ParseObject> meetingsPOList = meetingDAO
					.getMeetingsForUser(ParseUser.getCurrentUser().getEmail());

			// make a list of meetings including response
			for (int i = 0; i < meetingsPOList.size(); i++) {
				ParseObject tempMeetingPO = meetingsPOList.get(i);
				Meeting tempMeeting = globalVariable
						.convertPOtoMeeting(meetingsPOList.get(i));
				ParseObject checkResponsePO = responseToMeetingDAO
						.getCurrentResponseForMeeting(
								ParseUser.getCurrentUser(), tempMeetingPO);
				if (checkResponsePO != null) {
					tempMeeting.setHasBeenResponsedTo(true);
					tempMeeting.setCurrentResponse(checkResponsePO
							.getBoolean("isAttending"));
				}
				meetingList.add(tempMeeting);
			}// for()
			globalVariable.setMeetingList(meetingList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}// onPostExec

	}// Asyn

	private class FetchMeetingsOwnAsynTask extends
			AsyncTask<Boolean, Void, Void> {

		@Override
		protected Void doInBackground(Boolean... params) {

			List<Meeting> meetingList = new ArrayList<Meeting>();
			List<ParseObject> meetingsPOList = meetingDAO
					.getOwnMeetingsForUser(ParseUser.getCurrentUser());

			System.out.println("size is  :  " + meetingsPOList.size());
			// make a list of meetings including response
			for (int i = 0; i < meetingsPOList.size(); i++) {
				meetingsPOList.get(i).put("from", ParseUser.getCurrentUser());
				ParseObject tempMeetingPO = meetingsPOList.get(i);
				Meeting tempMeeting = globalVariable
						.convertPOtoMeeting(tempMeetingPO);
				List<ParseObject> allResponsesPO = responseToMeetingDAO
						.getAllResponsesForMeeting(tempMeetingPO);
				if (allResponsesPO != null) {
					tempMeeting.setResponses(allResponsesPO);
					System.out.println("not nullla");
				} else {
					System.out.println("nullla");
				}
				meetingList.add(tempMeeting);
			}// for()
			globalVariable.setMeetingOwnList(meetingList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}// onPostExec

	}// Asyn

	private class FetchAllUsersAsynTask extends AsyncTask<Boolean, Void, Void> {

		@Override
		protected Void doInBackground(Boolean... params) {
			List<ParseUser> userList = userDAO.getAllUsers();

			for (int i = 0; i < userList.size(); i++) {
				if (userList.get(i).getObjectId()
						.equals(ParseUser.getCurrentUser().getObjectId())) {
					userList.remove(i);
					break;
				}
			}
			globalVariable.setUserList(userList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}// onPostExecute()

	}// Asyn

}
