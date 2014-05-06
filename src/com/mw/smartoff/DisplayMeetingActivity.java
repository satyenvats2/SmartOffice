package com.mw.smartoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.mw.smartoff.DAO.MeetingDAO;
import com.mw.smartoff.DAO.ResponseToMeetingDAO;
import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.CharacterDrawable;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.HashMap;

public class DisplayMeetingActivity extends Activity {

	Button updateB;
	LinearLayout acceptRejectLL;

	TextView meetingSubjectTV;
	TextView senderNameTV;
	TextView senderEmailIDTV;
	TextView messageTV;
	TextView timeTV;
	TextView locationTV;
	TableLayout responsesTL;

	RelativeLayout footerMeetingRL;
	ImageView sendersIV;

	Intent previousIntent;
	Intent nextIntent;

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
	View tableRowView;
	View displayNotesView;

	GlobalVariable globalVariable;

	HashMap<String, Integer> myMap;
	
	private void findThings() {
		updateB = (Button) findViewById(R.id.update_Button);
		acceptRejectLL = (LinearLayout) findViewById(R.id.accept_reject_LL);
		meetingSubjectTV = (TextView) findViewById(R.id.meeting_subject_TV);
		senderNameTV = (TextView) findViewById(R.id.sender_name_TV);
		senderEmailIDTV = (TextView) findViewById(R.id.sender_emailID_TV);
		messageTV = (TextView) findViewById(R.id.message_real_message_TV);
		timeTV = (TextView) findViewById(R.id.time_TV);
		locationTV = (TextView) findViewById(R.id.location_TV);
		footerMeetingRL = (RelativeLayout) findViewById(R.id.footer_meeting_RL);

		responsesTL = (TableLayout) findViewById(R.id.responses_TL);
		sendersIV = (ImageView) findViewById(R.id.senders_IV);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		previousIntent = getIntent();
		if (previousIntent.getIntExtra("type", -1) == GlobalVariable.MEETINGS_ALL) {
			selectedMeeting = globalVariable.getMeetingList().get(
					(previousIntent.getIntExtra("position", -1)));
			System.out.println("MEETINGS_ALL");
			// Toast.makeText(this, "MEETINGS_ALL", Toast.LENGTH_SHORT).show();
		} else if (previousIntent.getIntExtra("type", -1) == GlobalVariable.MEETINGS_PENDING) {
			selectedMeeting = globalVariable.getMeetingPendingList().get(
					(previousIntent.getIntExtra("position", -1)));
			System.out.println("MEETINGS_PENDING");
			// Toast.makeText(this, "MEETINGS_PENDING",
			// Toast.LENGTH_SHORT).show();
		} else if (previousIntent.getIntExtra("type", -1) == GlobalVariable.MEETINGS_MY) {
			selectedMeeting = globalVariable.getMeetingOwnList().get(
					(previousIntent.getIntExtra("position", -1)));
			System.out.println("MEETINGS_MY");
			// Toast.makeText(this, "MEETINGS_MY", Toast.LENGTH_SHORT).show();
		}
		dao = new MeetingDAO(this);
		dao2 = new ResponseToMeetingDAO(this);
		createDialog = new CreateDialog(this);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		String[] alphabets = getResources().getStringArray(
				R.array.alphabets);
		int[] hexCodes = getResources().getIntArray(R.array.hex_codes);
		myMap = new HashMap<String, Integer>();
		for (int i = 0; i < alphabets.length; i++) {
			myMap.put(alphabets[i], hexCodes[i]);
		}
	}

	public void onSeeNotes(View view) {
		if (selectedMeeting.getResponses().get((Integer) view.getTag())
				.getString("notes") == null)
			Toast.makeText(this, "No notes by this user", Toast.LENGTH_SHORT)
					.show();
		else {
			alertDialog.setView(displayNotesView);
			alertDialog.show();
		}

	}

	private void initialVisibilityOfViews() {

		// if(my meetings)
		if (selectedMeeting.getFrom().equals(
				globalVariable.convertParseObjectToUser(ParseUser
						.getCurrentUser()))) {
			alertDialogBuilder = createDialog.createAlertDialog("Notes", null,
					false);
			alertDialogBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			displayNotesView = inflater.inflate(R.layout.display_notes, null,
					false);

			alertDialog = alertDialogBuilder.create();

			footerMeetingRL.setVisibility(View.GONE);
			responsesTL.setVisibility(View.VISIBLE);
			if (selectedMeeting == null)
				System.out.println("im im null");
			else
				System.out.println("im im not null");
			if (selectedMeeting.getResponses() != null)
				for (int i = 0; i < selectedMeeting.getResponses().size(); i++) {
					ParseObject tempResponsePO = selectedMeeting.getResponses()
							.get(i);
					tableRowView = inflater.inflate(R.layout.response_element,
							null, false);
					tableRowView.setTag(i);
					((TextView) tableRowView.findViewById(R.id.username_TV))
							.setText(tempResponsePO
									.getParseUser("responseFrom").getString(
											"name")
									+ " is ");
					TextView dsa = (TextView) tableRowView
							.findViewById(R.id.status_tv);
					if (tempResponsePO.getBoolean("isAttending") == true) {
						dsa.setText("attending.");
						dsa.setTextColor(Color.parseColor("#0000FF"));
					} else {
						dsa.setText("not attending.");
						dsa.setTextColor(Color.parseColor("#FF0000"));
					}
					System.out.println(tempResponsePO.has("notes")
							+ "dfsadfdsaf" + tempResponsePO.getString("notes"));
					if (tempResponsePO.getString("notes") == null) {
						((ImageView) tableRowView.findViewById(R.id.notes_IV))
								.setVisibility(View.INVISIBLE);
					} else {
						TextView tvTV = (TextView) displayNotesView
								.findViewById(R.id.display_notes_TV);
						tvTV.setTextSize(18);
						tvTV.setText(tempResponsePO.getString("notes"));
					}
					responsesTL.addView(tableRowView);
				}
		} else {
			alertDialogBuilder = createDialog.createAlertDialog("Add Notes",
					null, false);
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

			if (selectedMeeting.isHasBeenResponsedTo()) {
				acceptRejectLL.setVisibility(View.INVISIBLE);
			} else
				updateB.setVisibility(View.GONE);
		}
		meetingSubjectTV.setText(selectedMeeting.getSubject());
		senderNameTV.setText(selectedMeeting.getFrom().getName());
		senderEmailIDTV.setText(selectedMeeting.getFrom().getEmail());
		messageTV.setText(selectedMeeting.getContent());
		timeTV.setText(selectedMeeting.getStartTime().toString());
		locationTV.setText(selectedMeeting.getLocation());
		CharacterDrawable drawable = new CharacterDrawable(selectedMeeting
				.getFrom().getName().toUpperCase().charAt(0), myMap.get(selectedMeeting
						.getFrom().getName().toUpperCase().charAt(0)+""));
		sendersIV.setImageDrawable(drawable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_meeting);
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
						selectedMeeting.setHasBeenResponsedTo(true);
						selectedMeeting.setCurrentResponse(isAttending);
						System.out.println(">> response in GV : " + globalVariable.getMeetingList().get(
								previousIntent.getIntExtra("position", -1)).isCurrentResponse());
						
						selectedMeetingPO = dao.getMeetingByID(selectedMeeting
								.getID());
						if (notesET.getText().toString().trim().length() > 0)
							dao2.repondToMeeting(ParseUser.getCurrentUser(),
									selectedMeetingPO, isAttending, notesET
											.getText().toString().trim());
						else
							dao2.repondToMeeting(ParseUser.getCurrentUser(),
									selectedMeetingPO, isAttending, null);

						acceptRejectLL.setVisibility(View.INVISIBLE);
						updateB.setVisibility(View.VISIBLE);

						dialog.dismiss();

						if (isAttending) {
//							Calendar cal = Calendar.getInstance();
//							cal.setTime(selectedMeeting.getStartTime());
//							Intent intent = new Intent(Intent.ACTION_EDIT);
//							intent.setType("vnd.android.cursor.item/event");
//							intent.putExtra("beginTime", cal.getTimeInMillis());
//							intent.putExtra("allDay", false);
//							intent.putExtra("endTime",
//									cal.getTimeInMillis() + 60 * 60 * 1000);
//							intent.putExtra("title",
//									selectedMeeting.getSubject());
//							intent.putExtra("description",
//									selectedMeeting.getContent());
//							System.out.println("hello");
//							System.out.println("hello"
//									+ selectedMeeting.getLocation());
//							System.out.println("hello");
//							intent.putExtra(Events.EVENT_LOCATION,
//									selectedMeeting.getLocation());
//
//							startActivity(intent);
                            //
                            ContentValues cv = new ContentValues();
                            cv.put(Events.CALENDAR_ID, 1);
                            cv.put(Events.TITLE, selectedMeeting.getSubject());
                            cv.put(Events.DESCRIPTION, selectedMeeting.getContent());
                            cv.put(Events.EVENT_LOCATION, selectedMeeting.getLocation());

                            Calendar cal = Calendar.getInstance();
                            TimeZone tz = cal.getTimeZone();
							cal.setTime(selectedMeeting.getStartTime());
                            cv.put(Events.DTSTART, cal.getTimeInMillis());
                            cv.put(Events.DTEND, cal.getTimeInMillis() + 60 * 60 * 1000);
                            cv.put(Events.EVENT_TIMEZONE, tz.getDisplayName());

                            ContentResolver cr = getContentResolver();
                            Uri uri = cr.insert(Events.CONTENT_URI, cv);
                            System.out.println("Event URI ["+uri+"]");
                            previousIntent.putExtra("isAttending", isAttending);
                        }
					}
				});

		alertDialog = alertDialogBuilder.create();
		if ((ViewGroup) notesET.getParent() != null)
			((ViewGroup) notesET.getParent()).removeView(notesET);
		alertDialog.setView(notesET);
		alertDialog.show();
	}

	public void onUpdate(View view) {
		acceptRejectLL.setVisibility(View.VISIBLE);
		updateB.setVisibility(View.GONE);
	}

	public void onBack(View view) {
		GlobalVariable.RESPONDED_TO_MEETING = true;
		finish();
	}

	@Override
	public void onBackPressed() {
		this.setResult(RESULT_OK, previousIntent);
		GlobalVariable.RESPONDED_TO_MEETING = true;
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
		if (GlobalVariable.PIN != 0)
			GlobalVariable.PIN--;
	}

}
