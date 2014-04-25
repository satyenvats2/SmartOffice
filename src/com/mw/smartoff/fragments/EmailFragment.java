package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;

//import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mw.smartoff.DisplayEmailActivity;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.adapter.EmailsAdapter;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
		View rootView = inflater.inflate(R.layout.email_list_fragment,
				container, false);
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

			List<Email> emailList = new ArrayList<Email>();
			List<ParseObject> emailPOList = dao.getEmailsForUser(ParseUser
					.getCurrentUser().getEmail());
			if (emailPOList != null) {
				for (int i = 0; i < emailPOList.size(); i++) {
					ParseObject tempEmailPO = emailPOList.get(i);
					Email tempMeeting = globalVariable
							.convertPOtoEmail(tempEmailPO);
					emailList.add(tempMeeting);
				}
				globalVariable.setEmailList(emailList);
			}
			// globalVariable.setEmailList(emailList);
			// System.out.println("size is : " + asdf.size());
			return emailPOList;
		}

		@Override
		protected void onPostExecute(final List<ParseObject> emailPOList) {
			super.onPostExecute(emailPOList);
			final List<Email> emailList = globalVariable.getEmailList();
			if (emailList.size() == 0) {
				notifyEmailTV.setText("No emails found");
				notifyEmailTV.setVisibility(View.VISIBLE);
			} else {
				adapter = new EmailsAdapter(getActivity(), emailList);
				emailLV.setAdapter(adapter);

				emailLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						if (!emailList.get(position).isEmailRead()) {

							emailList.get(position).setEmailRead(true);
							adapter.swapData(emailList);
							adapter.notifyDataSetChanged();

							MarkEmailAsReadAsynTask asynTask = new MarkEmailAsReadAsynTask();
							asynTask.execute(new ParseObject[] { emailPOList
									.get(position) });
						}
						nextIntent = new Intent(getActivity(),
								DisplayEmailActivity.class);
						nextIntent.putExtra("position", position);
						startActivity(nextIntent);
					}
				});
			}
		}

	}// Asyn

	private class MarkEmailAsReadAsynTask extends
			AsyncTask<ParseObject, Void, Void> {
		// ParseUser user;
		@Override
		protected Void doInBackground(ParseObject... params) {
			ParseObject emailPO = params[0];
			emailPO.put("isMailRead", true);
			emailPO.saveEventually(new SaveCallback() {

				@Override
				public void done(ParseException arg0) {
					Toast.makeText(getActivity(), "mark as read",
							Toast.LENGTH_SHORT).show();

				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void emailPOList) {
			super.onPostExecute(emailPOList);

		}

	}
}
