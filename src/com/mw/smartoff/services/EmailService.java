package com.mw.smartoff.services;

import java.util.ArrayList;
import java.util.List;

import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.model.Email;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class EmailService extends IntentService {

	EmailDAO dao;
	GlobalVariable globalVariable;

	public EmailService() {
		super("EmailService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new EmailDAO(this);
		globalVariable = (GlobalVariable) this.getApplicationContext();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		List<Email> emailList = new ArrayList<Email>();
		List<ParseObject> emailPOList = dao.getEmailsForUser(ParseUser
				.getCurrentUser().getEmail());
		if (emailPOList != null) {
			for (int i = 0; i < emailPOList.size(); i++) {
				ParseObject tempEmailPO = emailPOList.get(i);
				Email tempEmail = globalVariable.convertPOtoEmail(tempEmailPO);
				emailList.add(tempEmail);
			}
			globalVariable.setEmailList(emailList);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent nextIntent = new Intent("new_email");
		LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
	}
}
