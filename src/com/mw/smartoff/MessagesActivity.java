package com.mw.smartoff;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.CharacterDrawable;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

import java.util.List;

public class MessagesActivity extends ListActivity {

	TextView messagesET;

    ParseUser selectedContactPU;

	Intent previousIntent;
	
	GlobalVariable globalVariable;

	private void findThings() {

        messagesET = (TextView) findViewById(R.id.message_ET);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		selectedContactPU = globalVariable.getUserList().get(
				(previousIntent.getIntExtra("position", -1)));
	}

	private void initialVisibilityOfViews() {

//		CharacterDrawable drawable = new CharacterDrawable(selectedEmail
//				.getFrom().getUsername().charAt(0), 0xFF805781);
//		sendersIV.setImageDrawable(drawable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages_list);
		findThings();
		initThings();
		initialVisibilityOfViews();
	}

}
