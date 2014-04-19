package com.mw.smartoff;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mw.smartoffice.R;

public class DisplayMeetingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_view);
	}

	
	public void onAccept(View view) 
	{
		Toast.makeText(this, "Put accept functionality", Toast.LENGTH_SHORT).show();
	}
	
	public void onReject(View view) 
	{
		Toast.makeText(this, "Put accept functionality", Toast.LENGTH_SHORT).show();
	}
}
