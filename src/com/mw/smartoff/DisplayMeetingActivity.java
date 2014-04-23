package com.mw.smartoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class DisplayMeetingActivity extends Activity {

	Button acceptB;
	Button rejectB;
	Button updateB;

	Intent previousIntent;
	Meeting selectedMeeting;

	MeetingDAO dao;
	ResponseToMeetingDAO dao2;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	EditText notesET;
	LinearLayout.LayoutParams lp;

	private void findThings() {
		acceptB = (Button) findViewById(R.id.accept_Button);
		rejectB = (Button) findViewById(R.id.reject_Button);
		updateB = (Button) findViewById(R.id.update_Button);
	}

	private void initThings() {
		previousIntent = getIntent();
		selectedMeeting = (Meeting) previousIntent
				.getSerializableExtra("selected_meeting");
		dao = new MeetingDAO(this);
		dao2 = new ResponseToMeetingDAO(this);
		createDialog = new CreateDialog(this);
		// createDialog.
		alertDialogBuilder = createDialog.createAlertDialog("Add Notes", null,
				false);
		alertDialogBuilder.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		notesET = new EditText(this);
		notesET.setHint("Add notes...");
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		notesET.setLayoutParams(lp);
	}

	private void initialVisibilityOfViews() {
		if (selectedMeeting.isHasBeenResponsedTo()) {
			acceptB.setVisibility(View.INVISIBLE);
			rejectB.setVisibility(View.INVISIBLE);
		} else
			updateB.setVisibility(View.INVISIBLE);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_view);
		findThings();
		initThings();
		initialVisibilityOfViews();
	}

	ParseObject selectedMeetingPO;

	public void onAccept(View view) {
		Toast.makeText(this, "accept", Toast.LENGTH_SHORT).show();
		showPopupForNotes("Accept Invite", true);
	}

	public void onReject(View view) {
		Toast.makeText(this, "reject", Toast.LENGTH_SHORT).show();
		showPopupForNotes("Reject Invite", false);
		// selectedMeetingPO = dao.getMeetingByID(selectedMeeting.getID());
		//
		// alertDialogBuilder.setNegativeButton("Reject Invite",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		//
		// selectedMeetingPO = dao.getMeetingByID(selectedMeeting
		// .getID());
		// if (notesET.getText().toString().trim().length() > 0)
		// dao2.repondToMeeting(ParseUser.getCurrentUser(),
		// selectedMeetingPO, false, notesET.getText()
		// .toString().trim());
		// else
		// dao2.repondToMeeting(ParseUser.getCurrentUser(),
		// selectedMeetingPO, true, null);
		// dialog.dismiss();
		// }
		// });
		//
		// alertDialog = alertDialogBuilder.create();
		// if ((ViewGroup) notesET.getParent() != null)
		// ((ViewGroup) notesET.getParent()).removeView(notesET);
		// alertDialog.setView(notesET);
		// alertDialog.show();
	}

	private void showPopupForNotes(String buttonTitle, final boolean isAttending) {
		alertDialogBuilder.setNegativeButton(buttonTitle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						selectedMeetingPO = dao.getMeetingByID(selectedMeeting
								.getID());
						if (notesET.getText().toString().trim().length() > 0)
							dao2.repondToMeeting(ParseUser.getCurrentUser(),
									selectedMeetingPO, isAttending, notesET
											.getText().toString().trim());
						else
							dao2.repondToMeeting(ParseUser.getCurrentUser(),
									selectedMeetingPO, isAttending, null);
						acceptB.setVisibility(View.INVISIBLE);
						rejectB.setVisibility(View.INVISIBLE);
						updateB.setVisibility(View.VISIBLE);
						
						dialog.dismiss();
					}
				});

		alertDialog = alertDialogBuilder.create();
		if ((ViewGroup) notesET.getParent() != null)
			((ViewGroup) notesET.getParent()).removeView(notesET);
		alertDialog.setView(notesET);
		alertDialog.show();
	}

	public void onUpdate(View view) {
		Toast.makeText(this, "Put update functionality", Toast.LENGTH_SHORT)
				.show();
		acceptB.setVisibility(View.VISIBLE);
		rejectB.setVisibility(View.VISIBLE);
		updateB.setVisibility(View.INVISIBLE);
	}
}
