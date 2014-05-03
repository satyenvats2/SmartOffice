package com.mw.smartoff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.CharacterDrawable;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;

public class DisplayEmailActivity extends Activity {

	TextView emailSubjectTV;
	ImageView sendersIV;
	TextView senderNameTV;
	TextView senderEmailIDTV;
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

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		selectedEmail = globalVariable.getEmailList().get(
				(previousIntent.getIntExtra("position", -1)));

	}

	private void initialVisibilityOfViews() {

		emailSubjectTV.setText(selectedEmail.getSubject());
		senderNameTV.setText(selectedEmail.getFrom().getUsername());
		senderEmailIDTV.setText(selectedEmail.getFrom().getEmail());
		messageRealMessageTV.setText(selectedEmail.getContent());
		CharacterDrawable drawable = new CharacterDrawable(selectedEmail
				.getFrom().getUsername().charAt(0), 0xFF805781);
		sendersIV.setImageDrawable(drawable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_email);
		findThings();
		initThings();
		initialVisibilityOfViews();
	}

	public void onBack(View view)
	{
		finish();
	}
}
