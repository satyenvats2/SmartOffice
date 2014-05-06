package com.mw.smartoff;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mw.smartoff.DAO.MessageDAO;
import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.adapter.MessagesAdapter;
import com.mw.smartoff.model.Message;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class DisplayMessagesActivity extends ListActivity {

	TextView messagesET;

	ParseUser selectedContactPU;

	Intent previousIntent;

	GlobalVariable globalVariable;
	MessageDAO dao;
	TextView notificationTV;

	MessagesAdapter adapter;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	private void findThings() {
		notificationTV = (TextView) findViewById(R.id.notification_TV);
		messagesET = (TextView) findViewById(R.id.message_ET);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		dao = new MessageDAO(this);
        if (previousIntent.hasExtra("fromUserId")){
            selectedContactPU = new UserDAO(this).getUserById(previousIntent.getStringExtra("fromUserId"));
        } else {
            selectedContactPU = globalVariable.getUserList().get(
                    (previousIntent.getIntExtra("position", -1)));
        }
		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Loading",
				"Fetching Meetings", true, null);
	}

	private void initialVisibilityOfViews() {

		// CharacterDrawable drawable = new CharacterDrawable(selectedEmail
		// .getFrom().getUsername().charAt(0), 0xFF805781);
		// sendersIV.setImageDrawable(drawable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages_list);
		findThings();
		initThings();
		initialVisibilityOfViews();

		progressDialog.show();
		FetchMsgsAsynTask asynTask = new FetchMsgsAsynTask();
		asynTask.execute(new String[] { "Helelo Worldsdfsdd" });
		
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
	}

	List<Message> msgsList;

	private class FetchMsgsAsynTask extends
			AsyncTask<String, Void, Void> {
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (msgsList == null || msgsList.size() == 0) {
				notificationTV.setVisibility(View.VISIBLE);
			} else {
				adapter = new MessagesAdapter(DisplayMessagesActivity.this, msgsList);
				setListAdapter(adapter);
			}
			progressDialog.dismiss();
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
				selectedContactPU, messagesET.getText().toString().trim());
		msgsList.add(tempMessage);
		adapter.notifyDataSetChanged();
		messagesET.setText("");

		SaveMsgAsynTask asynTask = new SaveMsgAsynTask();
		asynTask.execute(new String[] { messagesET.getText().toString().trim() });

	}
	
	Intent nextIntent;

    @Override
    public void onResume()
    {
        super.onResume();
        if (GlobalVariable.PIN == 0) {
			nextIntent = new Intent(this, VerifyPinActivity.class);
			startActivity(nextIntent);
		}
        GlobalVariable.PIN++;
    }

    @Override
    public void onRestart(){

        super.onRestart();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop(){

        super.onStop();
        GlobalVariable.PIN--;
    }
}
