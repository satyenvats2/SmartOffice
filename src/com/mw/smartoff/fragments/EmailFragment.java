package com.mw.smartoff.fragments;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mw.smartoff.LoginActivity;
import com.mw.smartoff.MainActivity;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.adapter.EmailsAdapter;
import com.mw.smartoff.model.User;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class EmailFragment extends Fragment {
	ListView emailLV;
	GlobalVariable globalVariable;
	EmailDAO dao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.email_fragment, container,
				false);
		return rootView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();
		FetchEmailsAsynTask asynTask = new FetchEmailsAsynTask();
		asynTask.execute(new String[] { "Hello World" });

		// EmailsAdapter adapter = new EmailsAdapter();
	}

	private void findThings() {
		emailLV = (ListView) getActivity().findViewById(R.id.email_LV);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new EmailDAO(getActivity());
	}

	private class FetchEmailsAsynTask extends AsyncTask<String, Void, Void> {
		// ParseUser user;
		@Override
		protected Void doInBackground(String... params) {
//			if (dao == null)
//				System.out.println("im null1");
//			else
//				System.out.println("not null");
//			if (globalVariable == null)
//				System.out.println("im null2");
//			else
//				System.out.println("not null");
//			if (globalVariable.getUser() == null)
//				System.out.println("im null3");
//			else
//				System.out.println("not null");
			
			List<ParseObject> emailPOList = dao.getEmailsForUser("user1@gmail.com");
			adapter = new EmailsAdapter(getActivity(), emailPOList);
//			System.out.println("size is : " + asdf.size());
			return null;
		}
		EmailsAdapter adapter;
		@Override
		protected void onPostExecute(Void user) {
			super.onPostExecute(user);
			emailLV.setAdapter(adapter);
		}

	}
}
