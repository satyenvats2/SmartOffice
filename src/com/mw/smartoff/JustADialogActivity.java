package com.mw.smartoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;

public class JustADialogActivity extends Activity {
	CreateDialog createDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	Intent nextIntent;

	int notificationType = 0;
	String userId = null;

	GlobalVariable globalVariable;
	Intent previousIntent;

	String dialogNoteTitle = null;
	String dialogNoteMessage = null;

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		createDialog = new CreateDialog(this);
		previousIntent = getIntent();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.broadcast_alert);
		initThings();

		Bundle extras = previousIntent.getExtras();
		if (extras != null) {
			notificationType = extras.getInt("type");
			userId = extras.getString("fromUserId");
			System.out.println(">>>>>>>" + notificationType + "<<<<<<<" + userId);
		}

		switch (notificationType) {
		case 0:
			nextIntent = new Intent(this, MainActivity.class);
			nextIntent.putExtra("type", notificationType);

			dialogNoteTitle = "New Email";
			dialogNoteMessage = "You have a new Email";
			showDialog(dialogNoteTitle, dialogNoteMessage);
			break;
		case 1:
			nextIntent = new Intent(this, MainActivity.class);
			nextIntent.putExtra("type", notificationType);

			dialogNoteTitle = "New Meeting Invite";
			dialogNoteMessage = "You have a new Meeting Invite";
			showDialog(dialogNoteTitle, dialogNoteMessage);
			break;
		case 2:
			nextIntent = new Intent(this, MainActivity.class);
			nextIntent.putExtra("type", notificationType);

			if (globalVariable.getChatPerson() != null) {
				if (userId.equals(globalVariable.getChatPerson().getObjectId())) {
					// refresh chat list
					Intent intent = new Intent("new_message");
					// add data
//					intent.putExtra("message", "data");
					LocalBroadcastManager.getInstance(this).sendBroadcast(
							intent);
				} else {
					// update preferences for contacts page

					// if(contacts page is open)
					// {
					// refresh contacts page
					// }
				}
			}

			// dialogNoteTitle = "New Message";
			// dialogNoteMessage = "You have a new Message";
			break;
		default:
			break;
		}

	}

	private void showDialog(String dialogNoteTitle, String dialogNoteMessage) {
		alertDialogBuilder = createDialog.createAlertDialog(dialogNoteTitle,
				dialogNoteMessage, false);
		alertDialogBuilder.setPositiveButton("Read now",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						startActivity(nextIntent);
					}
				});
		alertDialogBuilder.setNegativeButton("Read it later",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
