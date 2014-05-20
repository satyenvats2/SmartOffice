package com.mw.smartoff;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.adapter.AttendanceAdapter;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.services.AttendanceService;
import com.mw.smartoffice.R;

public class DisplayAttendanceActivity extends Activity {

	Date selectedDate;
	// AttendanceDAO dao;
	Intent serviceIntent;

	EditText searchET;
	PullAndLoadListView attendanceLV;
	ProgressBar progressBar;

	AttendanceAdapter adapter;
Intent nextIntent;
	// List<ParseObject> attendanceListPO;

	GlobalVariable globalVariable;

	private BroadcastReceiver emailReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			progressBar.setVisibility(View.GONE);
			// attendanceListPO = globalVariable.getAttendancePOList();
			attendanceLV.onRefreshComplete();
			adapter.swapData();
			adapter.notifyDataSetChanged();
			Toast.makeText(context, "AttendFrag broad response",
					Toast.LENGTH_SHORT).show();
		}
	};

	private void findThings() {
		searchET = (EditText) findViewById(R.id.search_ET);
		attendanceLV = (PullAndLoadListView) findViewById(R.id.attendance_LV);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		// dao = new AttendanceDAO(this);
		selectedDate = new Date();
		serviceIntent = new Intent(this, AttendanceService.class);
		adapter = new AttendanceAdapter(this, globalVariable.getUserList());
		attendanceLV.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_attendance);
		findThings();
		initThings();

		searchET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				System.out.println("Text [" + s + "]");
				adapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		attendanceLV
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						serviceIntent.putExtra("selectedDate",
								formatDate(selectedDate).getTime());
						startService(serviceIntent);
					}
				});

		serviceIntent.putExtra("selectedDate", formatDate(selectedDate)
				.getTime());
		startService(serviceIntent);
	}

	@SuppressLint("SimpleDateFormat")
	private Date formatDate(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

		String dateStr = formatter.format(date);

		Date date2 = null;
		try {
			date2 = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (GlobalVariable.PIN == 0) {
			nextIntent = new Intent(this, VerifyPinActivity.class);
			startActivity(nextIntent);
		}
		GlobalVariable.PIN++;
		LocalBroadcastManager.getInstance(this).registerReceiver(emailReceiver,
				new IntentFilter("attendance_list"));
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				emailReceiver);
	}

	@Override
	public void onStop() {
		super.onStop();
		GlobalVariable.PIN--;
	}
}
