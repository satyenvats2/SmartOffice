package com.mw.smartoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.CharacterDrawable;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class DisplayMeetingActivity extends Activity {

	Button acceptB;
	Button rejectB;
	Button updateB;

	TextView meetingSubjectTV;
	TextView senderNameTV;
	TextView senderEmailIDTV;
	TextView messageTV;
	LinearLayout responsesRL;
	RelativeLayout footerMeetingRL;
	ImageView sendersIV;

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

	ParseObject selectedMeetingPO;

	LayoutInflater inflater;
	View child;

	GlobalVariable globalVariable;

	private void findThings() {
		acceptB = (Button) findViewById(R.id.accept_Button);
		rejectB = (Button) findViewById(R.id.reject_Button);
		updateB = (Button) findViewById(R.id.update_Button);
		meetingSubjectTV = (TextView) findViewById(R.id.header_meeting_subject_TV);
		senderNameTV = (TextView) findViewById(R.id.sender_name_TV);
		senderEmailIDTV = (TextView) findViewById(R.id.sender_emailID_TV);
		messageTV = (TextView) findViewById(R.id.message_real_message_TV);
		footerMeetingRL = (RelativeLayout) findViewById(R.id.footer_meeting_RL);

		responsesRL = (LinearLayout) findViewById(R.id.responses_RL);
		sendersIV = (ImageView) findViewById(R.id.senders_IV);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		selectedMeeting = globalVariable.getMeetingList().get(
				(previousIntent.getIntExtra("position", -1)));
		dao = new MeetingDAO(this);
		dao2 = new ResponseToMeetingDAO(this);
		createDialog = new CreateDialog(this);
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

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private void initialVisibilityOfViews() {

		if (selectedMeeting.getFrom().equals(
				globalVariable.convertParseObjectToUser(ParseUser
						.getCurrentUser()))) {
			footerMeetingRL.setVisibility(View.GONE);
			responsesRL.setVisibility(View.VISIBLE);
			for (int i = 0; i < selectedMeeting.getResponses().size(); i++) {

				child = inflater
						.inflate(R.layout.response_element, null, false);
				((TextView) child.findViewById(R.id.username_TV))
						.setText(selectedMeeting.getResponses().get(i)
								.getParseUser("responseFrom").getUsername());
				if (selectedMeeting.getResponses().get(i)
						.getBoolean("isAttending") == true)
					((TextView) child.findViewById(R.id.status_tv))
							.setText("Attending");
				else
					((TextView) child.findViewById(R.id.status_tv))
							.setText("Not attending");

				responsesRL.addView(child);
			}
		} else {
			if (selectedMeeting.isHasBeenResponsedTo()) {
				acceptB.setVisibility(View.INVISIBLE);
				rejectB.setVisibility(View.INVISIBLE);
			} else
				updateB.setVisibility(View.INVISIBLE);
		}
		meetingSubjectTV.setText(selectedMeeting.getSubject());
		senderNameTV.setText(selectedMeeting.getFrom().getUsername());
		senderEmailIDTV.setText(selectedMeeting.getFrom().getEmail());
		messageTV.setText(selectedMeeting.getContent());
		CharacterDrawable drawable = new CharacterDrawable(selectedMeeting
				.getFrom().getUsername().charAt(0), 0xFF805781);
		sendersIV.setImageDrawable(drawable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_view);
		findThings();
		initThings();
		initialVisibilityOfViews();
	}

	public void onAccept(View view) {
		Toast.makeText(this, "accept", Toast.LENGTH_SHORT).show();
		previousIntent.putExtra("isAttending", true);
		showPopupForNotes("Accept Invite", true);
	}

	public void onReject(View view) {
		Toast.makeText(this, "reject", Toast.LENGTH_SHORT).show();
		previousIntent.putExtra("isAttending", false);
		showPopupForNotes("Reject Invite", false);
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

	@Override
	public void onBackPressed() {
		this.setResult(RESULT_OK, previousIntent);
		finish();
	}

}
