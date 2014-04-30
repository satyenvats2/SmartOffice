package com.mw.smartoff;

import com.mw.smartoff.services.CreateDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class JustADialogActivity extends Activity {
	CreateDialog createDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	createDialog = new CreateDialog(this);
	final Intent intent = new Intent(this, CreateEmailActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		alertDialogBuilder = createDialog.createAlertDialog("Notes", null,
				false);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
			            startActivity(intent);
					}
				});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		
		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	
		
		
	}

}
