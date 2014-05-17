package com.mw.smartoff.DAO;

import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AttendanceDAO {

	ParseQuery<ParseObject> query;
	Context context;

	public AttendanceDAO(Context context) {
		super();
		this.context = context;
		Parse.initialize(context, "wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("Attendance");
	}

	public void markYourAttendance() {
		ParseObject attendancePO = new ParseObject("Attendance");
		attendancePO.put("from", ParseUser.getCurrentUser());
		attendancePO.put("date", new Date());
		attendancePO.put("address", "dsadsa");
		attendancePO.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {
				Toast.makeText(context, "Your attendance is marked.",
						Toast.LENGTH_SHORT).show();

			}
		});
	}
}
