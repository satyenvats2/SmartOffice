package com.mw.smartoff.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.mw.smartoff.JustADialogActivity;

public class PushReceiver extends BroadcastReceiver {
	Intent nextIntent;
	GlobalVariable globalVariable;

	@Override
	public void onReceive(final Context context, Intent intent) {
		globalVariable = (GlobalVariable) context.getApplicationContext();

		Bundle extras = intent.getExtras();
		String message = extras != null ? extras.getString("com.parse.Data")
				: "";
		System.out.println(">>>>><<<<<<" + message);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(message);
			Toast.makeText(context,
					"Notif received - " + jsonObject.getInt("type"),
					Toast.LENGTH_LONG).show();
			nextIntent = new Intent(context, JustADialogActivity.class);
			nextIntent.putExtra("type", jsonObject.getInt("type"));
			nextIntent.putExtra("fromUserId",
					jsonObject.getString("fromUserId"));
			nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (jsonObject.getInt("type") == 2) {
				if (globalVariable.getChatPerson() != null) {
					if (jsonObject.getString("fromUserId").equals(
							globalVariable.getChatPerson().getObjectId())) {
						// refresh chat list
						Intent nextIntent = new Intent("new_message");
						// add data
						nextIntent.putExtra("message", jsonObject.getString("message"));
						LocalBroadcastManager.getInstance(context)
								.sendBroadcast(nextIntent);
					} else {
						// update preferences for contacts page

						// if(contacts page is open)
						// {
						// refresh contacts page
						// }
					}
				}

			} else {
				context.startActivity(nextIntent);
			}
			// context.startService(nextIntent);
			// context.sendBroadcast(nextIntent);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
