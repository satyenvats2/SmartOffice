package com.mw.smartoff.services;

import static android.widget.Toast.LENGTH_LONG;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mw.smartoff.JustADialogActivity;

public class PushReceiver extends BroadcastReceiver {

	
	
	@Override
    public void onReceive(final Context context, Intent intent) {
		
        Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "";
        JSONObject jObject;
        try {
            jObject = new JSONObject(message);
            Toast.makeText(context, "Smart Office - " + jObject.getString("type"), LENGTH_LONG).show();
            Intent i = new Intent(context, JustADialogActivity.class);
            i.putExtra("type", jObject.getString("type"));
            i.putExtra("fromUserId", jObject.getString("fromUserId"));
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        	context.startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
