package com.mw.smartoff.services;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.User;
import com.parse.ParseUser;

// called from ContactFragment, 
public class ContactService extends IntentService {

	UserDAO dao;
	GlobalVariable globalVariable;

	SharedPreferences sharedPreferences;
	Editor editor;

	Gson gson;

	public ContactService() {
		super("ContactService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new UserDAO(this);
		globalVariable = (GlobalVariable) this.getApplicationContext();

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		editor = sharedPreferences.edit();

		gson = new Gson();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {

		List<ParseUser> userListPU = dao.getAllUsers();

		if (userListPU != null && userListPU.size() > 0) {

			// remove logged in user from list of users
			for (int i = 0; i < userListPU.size(); i++) {
				if (userListPU.get(i).getObjectId()
						.equals(ParseUser.getCurrentUser().getObjectId())) {
					userListPU.remove(i);
					break;
				}
			}

//			Put list of users in preferences
//			String json = gson.toJson(usersListPU);
//			System.out.println("<><>"+json);
//			editor.putString("user_list", json);
			
			// If shared preferences don't have a particular user then
			// initialise
			// unread message count for that user to 0.
			for (int i = 0; i < userListPU.size(); i++) {
				if (!sharedPreferences.contains(userListPU.get(i).getObjectId())) {
					editor.putInt(userListPU.get(i).getObjectId(), 0);
				}
			}
			
			
			List<User> aa = globalVariable.convertPUListToUserList(userListPU);
			String json = gson.toJson(aa);
			System.out.println("<><>"+json);
			editor.putString("user_list", json);
			

			editor.commit();
			globalVariable.setUserList(userListPU);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent nextIntent = new Intent("contacts_count");
		LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
	}
}
