package com.mw.smartoff;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mw.smartoff.DAO.AttendanceDAO;
import com.mw.smartoff.services.AttendanceService;
import com.mw.smartoffice.R;

public class DisplayAttendanceActivity extends Activity {

	Date selectedDate;
	AttendanceDAO dao;
	Intent serviceIntent;

	private void findThings() {
	}

	private void initThings() {
		dao = new AttendanceDAO(this);
		selectedDate = new Date();
		serviceIntent = new Intent(this, AttendanceService.class);
	}

	private void roundOffDate() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_attendance);
		findThings();
		initThings();

		startService(serviceIntent);
	}

	@SuppressLint("SimpleDateFormat")
	private Date formatDate(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy zzzz");

		String dateStr = formatter.format(date);

		Date date2 = null;
		try {
			date2 = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

	Date getDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
}
