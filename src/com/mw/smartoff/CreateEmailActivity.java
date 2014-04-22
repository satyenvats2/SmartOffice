package com.mw.smartoff;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.adapter.UserAutoCompleteAdapter;
import com.mw.smartoff.services.UserAutoCompleteTextView;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CreateEmailActivity extends Activity {
	UserDAO dao;
	UserAutoCompleteTextView userACTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_create);
		dao = new UserDAO(this);
		userACTV = (UserAutoCompleteTextView) findViewById(R.id.to_TV);
		
		FetchAutoTextDataAsynTask asynTask = new FetchAutoTextDataAsynTask();
		asynTask.execute(new String[]{"Hello World"});
	}

	private class FetchAutoTextDataAsynTask extends
			AsyncTask<String, Void, List<ParseUser>> {

		@Override
		protected List<ParseUser> doInBackground(String... params) {
			
			List<ParseUser> userList = dao.getAllUsers();
			return userList;
		}

		@Override
		protected void onPostExecute(final List<ParseUser> usersList) {
			super.onPostExecute(usersList);
			if (usersList != null && usersList.size() > 0) {
				UserAutoCompleteAdapter adapter = new UserAutoCompleteAdapter(
						CreateEmailActivity.this, usersList);
				userACTV.setThreshold(1);
				userACTV.setAdapter(adapter);

				OnItemClickListener itemClickListener = new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						ParseUser temp = (ParseUser) arg0
								.getItemAtPosition(position);

						int pos = -1;

						for (int i = 0; i < usersList.size(); i++) {
							if (usersList.get(i).equals(temp)) {
								pos = i;
								break;
							}
						}
						System.out.println("Position " + pos);

					}
				};

				userACTV.setOnItemClickListener(itemClickListener);
			}
		}

	}//Asyn

}
