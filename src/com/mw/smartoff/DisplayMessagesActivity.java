package com.mw.smartoff;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mw.smartoff.DAO.MessageDAO;
import com.mw.smartoff.adapter.MessagesAdapter;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Message;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class DisplayMessagesActivity extends ListActivity {

	GlobalVariable globalVariable;

	TextView messagesET;

	TextView notificationTV;
	ProgressBar progressBar;

	Intent previousIntent;
	Intent nextIntent;

	ParseUser selectedContactPU;

	MessageDAO dao;
	List<Message> msgsList;
	MessagesAdapter adapter;

	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent

			Message tempMessage = new Message(null,
					globalVariable.getChatPerson(), ParseUser.getCurrentUser(),
					intent.getStringExtra("message"), new Date());
			msgsList.add(tempMessage);

			if (adapter == null) {
				adapter = new MessagesAdapter(DisplayMessagesActivity.this,
						msgsList);
				setListAdapter(adapter);

				// if A & B are chatting & there are no messages initially. So
				// on receiving the first message we have to remove the
				// notification label.
				if (notificationTV.isShown()) {
					notificationTV.setVisibility(View.INVISIBLE);
				}

			}
			adapter.notifyDataSetChanged();
			setSelection(adapter.getCount() - 1);
			System.out.println(">>>>>>>>>>>suyccesese");

		}
	};

	private void findThings() {
		notificationTV = (TextView) findViewById(R.id.notification_TV);
		messagesET = (TextView) findViewById(R.id.message_ET);
		progressBar = (ProgressBar) findViewById(R.id.progressBarMSG);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		dao = new MessageDAO(this);
		selectedContactPU = globalVariable.getChatPerson();
	}

	private void initialVisibilityOfViews() {
		setTitle(selectedContactPU.getString("name"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages_list);

		getActionBar().setIcon(android.R.color.transparent);

		findThings();
		initThings();
		initialVisibilityOfViews();

		// progressDialog.show();
		FetchMsgsAsynTask asynTask = new FetchMsgsAsynTask();
		asynTask.execute(new String[] { "HW" });

		((RelativeLayout) findViewById(R.id.messages_list_RL))
				.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), 0);
						return false;
					}
				});

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private class FetchMsgsAsynTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {

			msgsList = new ArrayList<Message>();
			List<ParseObject> msgsPOList = dao.getMsgsForUser(
					ParseUser.getCurrentUser(), selectedContactPU);
			if (msgsPOList != null) {
				for (int i = 0; i < msgsPOList.size(); i++) {
					ParseObject tempMsgPO = msgsPOList.get(i);
					Message tempMessage = globalVariable
							.convertPOtoMessage(tempMsgPO);
					msgsList.add(tempMessage);
				}
			} else {
				System.out.println("nulkla");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.INVISIBLE);
			if (msgsList == null || msgsList.size() == 0) {
				notificationTV.setVisibility(View.VISIBLE);
			} else {
				adapter = new MessagesAdapter(DisplayMessagesActivity.this,
						msgsList);
				setListAdapter(adapter);
			}
		}

	}// FetchMsgsAsynTask

	private class SaveMsgAsynTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			dao.saveMessage(ParseUser.getCurrentUser(), selectedContactPU,
					params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void msgsList) {
			super.onPostExecute(msgsList);
		}

	}// SaveMsgAsynTask

	public void onSend(View view) {
		if (messagesET.getText().toString().trim().length() == 0)
			return;
		Message tempMessage = new Message(null, ParseUser.getCurrentUser(),
				selectedContactPU, messagesET.getText().toString().trim(), null);
		msgsList.add(tempMessage);
		if (adapter == null) {
			adapter = new MessagesAdapter(DisplayMessagesActivity.this,
					msgsList);
			setListAdapter(adapter);
			notificationTV.setVisibility(View.INVISIBLE);
		} else
			adapter.notifyDataSetChanged();

		setSelection(adapter.getCount() - 1);

		SaveMsgAsynTask asynTask = new SaveMsgAsynTask();
		asynTask.execute(new String[] { messagesET.getText().toString().trim() });

		messagesET.setText("");

		// to close the keyboard
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public void onBack(View view) {
		globalVariable.setChatPerson(null);
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (GlobalVariable.PIN == 0) {
			nextIntent = new Intent(this, VerifyPinActivity.class);
			startActivity(nextIntent);
		}
		GlobalVariable.PIN++;

		LocalBroadcastManager.getInstance(this).registerReceiver(
				messageReceiver, new IntentFilter("new_message"));
	}

	@Override
	public void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				messageReceiver);
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		GlobalVariable.PIN--;
	}

	@Override
	public void onBackPressed() {
		globalVariable.setChatPerson(null);
		super.onBackPressed();
	}

	@Override
	public void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
