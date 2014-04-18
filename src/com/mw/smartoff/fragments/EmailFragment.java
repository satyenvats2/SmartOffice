package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mw.smartoff.DisplayEmailActivity;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.adapter.EmailsAdapter;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;

public class EmailFragment extends Fragment {
	ListView emailLV;
	GlobalVariable globalVariable;
	EmailDAO dao;
	TextView notifyEmailTV;

	EmailsAdapter adapter;
	Intent nextIntent;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.email_list_fragment, container,
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
	}

	private void findThings() {
		emailLV = (ListView) getActivity().findViewById(R.id.email_LV);
		notifyEmailTV = (TextView) getActivity().findViewById(
				R.id.notify_email_TV);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new EmailDAO(getActivity());
	}

	private class FetchEmailsAsynTask extends
			AsyncTask<String, Void, List<ParseObject>> {
		// ParseUser user;
		@Override
		protected List<ParseObject> doInBackground(String... params) {

			List<ParseObject> emailPOList = dao
					.getEmailsForUser("user1@gmail.com");
			// System.out.println("size is : " + asdf.size());
			return emailPOList;
		}


		@Override
		protected void onPostExecute(List<ParseObject> emailPOList) {
			super.onPostExecute(emailPOList);
			if (emailPOList.size() == 0) {
				notifyEmailTV.setText("No emails found");
				notifyEmailTV.setVisibility(View.VISIBLE);
			} else {
				List<Email> emailList = new ArrayList<Email>();
				for (int i = 0; i < emailPOList.size(); i++) {
					emailList.add(convertPOtoEmail(emailPOList.get(i)));
				}
				adapter = new EmailsAdapter(getActivity(), emailList);
				emailLV.setAdapter(adapter);

				emailLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						nextIntent = new Intent(getActivity(),
								DisplayEmailActivity.class);
						startActivity(nextIntent);
					}
				});
			}
		}

	}// Asyn

	private Email convertPOtoEmail(ParseObject emailPO) {
		return new Email(emailPO.getString("subject"),
				emailPO.getString("content"), emailPO.getBoolean("isMailRead"),
				emailPO.getCreatedAt());
	}

}
