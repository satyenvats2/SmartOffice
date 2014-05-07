package com.mw.smartoff;

import com.mw.smartoff.services.CreateDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.mw.smartoffice.R;

public class JustADialogActivity extends Activity {
	CreateDialog createDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;
	Intent nextIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.broadcast_alert);
		Intent previousIntent = getIntent();
		Bundle extras = previousIntent.getExtras();
		int notificationType = 0;
//        String userId = null;
		if (extras != null) {
			notificationType = extras.getInt("type");
//            userId = extras.getString("fromUserId");
			System.out.println(">>>>>>>" + notificationType );
		}

		String dialogNoteTitle = null;
		String dialogNoteMessage = null;

		switch (notificationType) {
		case 0:
//			nextIntent = new Intent(this, DisplayEmailActivity.class);
			nextIntent = new Intent(this, MainActivity.class);
			nextIntent.putExtra("type", notificationType);
			dialogNoteTitle = "New Email";
			dialogNoteMessage = "You have a new Email";
			break;
		case 1:
//			nextIntent = new Intent(this, DisplayMeetingActivity.class);
			nextIntent = new Intent(this, MainActivity.class);
			nextIntent.putExtra("type", notificationType);
			dialogNoteTitle = "New Meeting Invite";
			dialogNoteMessage = "You have a new Meeting Invite";
			break;
		case 2:
//			nextIntent = new Intent(this, DisplayMessagesActivity.class);
			nextIntent = new Intent(this, MainActivity.class);
			nextIntent.putExtra("type", notificationType);
//          nextIntent.putExtra("fromUserId", userId);
			dialogNoteTitle = "New Message";
			dialogNoteMessage = "You have a new Message";
			break;
		default:
			break;
		}

		createDialog = new CreateDialog(this);

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
