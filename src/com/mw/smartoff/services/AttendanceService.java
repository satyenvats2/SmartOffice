package com.mw.smartoff.services;

import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.mw.smartoff.DAO.AttendanceDAO;
import com.mw.smartoff.extras.GlobalVariable;
import com.parse.ParseObject;

public class AttendanceService extends IntentService {

	AttendanceDAO dao;
	GlobalVariable globalVariable;

	public AttendanceService() {
		super("AttendanceService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new AttendanceDAO(this);
		globalVariable = (GlobalVariable) this.getApplicationContext();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Date d = new Date();
		d.setTime(arg0.getLongExtra("selectedDate", -1));
		System.out.println("date is :  " + d);
		List<ParseObject> attendancePOList = dao.getAttendanceForDate(d,
				globalVariable.addToDate(d, 1));
		if (attendancePOList != null) {
			globalVariable.setAttendancePOList(attendancePOList);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent nextIntent = new Intent("attendance_list");
		LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
	}
}
