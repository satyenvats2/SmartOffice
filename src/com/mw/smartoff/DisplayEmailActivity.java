package com.mw.smartoff;

import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.CharacterDrawable;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayEmailActivity extends Activity {

	TextView headerEmailSubjectTV;
	ImageView sendersIV;
	TextView senderNameTV;
	TextView senderEmailIDTV;
	TextView messageRealMessageTV;

	Intent previousIntent;
	
	GlobalVariable globalVariable;
	
	Email selectedEmail;
	
	private void findThings() {

		headerEmailSubjectTV = (TextView) findViewById(R.id.header_email_subject_TV);
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

		headerEmailSubjectTV.setText(selectedEmail.getSubject());
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

}
