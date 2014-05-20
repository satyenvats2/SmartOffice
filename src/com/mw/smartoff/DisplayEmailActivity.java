package com.mw.smartoff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoff.extras.CharacterDrawable;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Email;
import com.mw.smartoffice.R;

public class DisplayEmailActivity extends Activity {

	TextView emailSubjectTV;
	ImageView sendersIV;
	TextView senderNameTV;
	TextView senderEmailIDTV;
	TextView sendDateTV;
	TextView messageRealMessageTV;

	Intent previousIntent;

	GlobalVariable globalVariable;

	Email selectedEmail;

	private void findThings() {

		emailSubjectTV = (TextView) findViewById(R.id.email_subject_TV);
		sendersIV = (ImageView) findViewById(R.id.senders_IV);
		senderNameTV = (TextView) findViewById(R.id.sender_name_TV);
		senderEmailIDTV = (TextView) findViewById(R.id.sender_emailID_TV);
		messageRealMessageTV = (TextView) findViewById(R.id.message_real_message_TV);
		sendDateTV = (TextView) findViewById(R.id.date_TV);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		selectedEmail = globalVariable.getEmailList().get(
				(previousIntent.getIntExtra("position", -1)));

	}

	private void initialVisibilityOfViews() {
		// setTitle(selectedEmail.getSubject());
		emailSubjectTV.setText(selectedEmail.getSubject());
		senderNameTV.setText(selectedEmail.getFrom().getName());
		senderEmailIDTV.setText(selectedEmail.getFrom().getEmail());
		sendDateTV.setText(globalVariable.getDisplayDate(selectedEmail
				.getCreatedAt()));
		messageRealMessageTV.setText(selectedEmail.getContent());

		CharacterDrawable drawable = new CharacterDrawable(selectedEmail
				.getFrom().getUsername().toUpperCase().charAt(0),
				globalVariable.getMyMap().get(
						selectedEmail.getFrom().getUsername().toUpperCase()
								.charAt(0)
								+ ""));
		sendersIV.setImageDrawable(drawable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_email);
		findThings();
		initThings();
		initialVisibilityOfViews();

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void onBack(View view) {
		finish();
	}

	Intent nextIntent;

	@Override
	public void onResume() {
		super.onResume();
		if (GlobalVariable.PIN == 0) {
			nextIntent = new Intent(this, VerifyPinActivity.class);
			startActivity(nextIntent);
		}
		GlobalVariable.PIN++;
	}

	@Override
	public void onRestart() {

		super.onRestart();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		GlobalVariable.PIN--;
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
