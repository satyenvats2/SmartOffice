package com.mw.smartoff;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.model.User;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	EditText emailET;
	EditText passwordET;

	UserDAO userDAO;

	GlobalVariable globalVariable;
	Intent nextIntent;

	SharedPreferences sharedPreferences;
	Editor editor;
	
	private void findThings() {
		emailET = (EditText) findViewById(R.id.email_ET);
		passwordET = (EditText) findViewById(R.id.password_ET);
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
		globalVariable = (GlobalVariable) getApplicationContext();
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this
						.getApplicationContext());
		editor = sharedPreferences.edit();
		nextIntent = new Intent(LoginActivity.this, MainActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");

//		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);

		// PushService.setDefaultPushCallback(this. YourActivity.class);
		// ParseInstallation.getCurrentInstallation().saveInBackground();
		setContentView(R.layout.login_page);
		findThings();
		myOwnListeners();
		initThings();

		if(ParseUser.getCurrentUser() != null)
		{
			System.out.println("usaer not null");
			startActivity(nextIntent);
		}

	}

	public void onLogin(View view) {
		if (!validate())
			return;
		LoginUserAsynTask asynTask = new LoginUserAsynTask();
		asynTask.execute(new String[] { "hello world" });
	}

	private boolean validate() {
		boolean bool = true;
		if (emailET.getText().toString().trim().length() < 5) {
			emailET.setError("Enter a valid email ID");
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
		@Override
		protected ParseUser doInBackground(String... params) {
			ParseUser user = userDAO.loginUser(emailET.getText().toString().trim(),
					passwordET.getText().toString());

			return user;
		}

		@Override
		protected void onPostExecute(ParseUser user) {
			super.onPostExecute(user);
			if (user != null) {
				System.out.println("im not null");
				Toast.makeText(LoginActivity.this, "Login Successfull",
						Toast.LENGTH_SHORT).show();
				User currentUser = new User(user.getEmail(), user.getUsername());
//				globalVariable.setUser(currentUser);
//				System.out.println("username : " + user.getEmail() + "email : " + globalVariable.getUser().getUsername());
//				nextIntent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(nextIntent);
			} else
				System.out.println("im nulll");
		}

	}
}
