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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayEmailActivity;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.adapter.EmailsAdapter;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EmailFragment extends Fragment {
	PullAndLoadListView emailLV;
	GlobalVariable globalVariable;
	EmailDAO dao;
	TextView notifyEmailTV;
	ProgressBar progressBar;

	EmailsAdapter adapter;
	Intent nextIntent;

	ParseQuery<ParseObject> query;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Parse.initialize(getActivity(),
				"wHhiiTucu7ntVNl3otR9f59eGg4UD1UavTlWvFzo",
				"sdGM0MdrbQjeVsha7pAFT9YL5WuUt7dA7f2zb0LW");
		query = ParseQuery.getQuery("Emails");

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
		asynTask.execute(true);
		// Set a listener to be invoked when the list should be refreshed.
		emailLV.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// Do work to refresh the list here.
				// new PullToRefreshDataTask().execute();
				new FetchEmailsAsynTask().execute(false);
			}
		});
	}

	private void findThings() {
		emailLV = (PullAndLoadListView) getActivity().findViewById(
				R.id.email_LV);
		notifyEmailTV = (TextView) getActivity().findViewById(
				R.id.notify_email_TV);
		progressBar = (ProgressBar) getActivity()
				.findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		if (globalVariable.getEmailList() != null) {
			progressBar.setVisibility(View.INVISIBLE);
		}
		dao = new EmailDAO(getActivity());
	}

	private class FetchEmailsAsynTask extends
			AsyncTask<Boolean, Void, List<ParseObject>> {
		// ParseUser user;
		@Override
		protected List<ParseObject> doInBackground(Boolean... params) {
			
			if (globalVariable.getEmailList() != null && params[0]) {
				return null;
			}
			System.out.println(">>>>>>>elseseses");
			List<Email> emailList = new ArrayList<Email>();
			List<ParseObject> emailPOList = dao.getEmailsForUser(ParseUser
					.getCurrentUser().getEmail());
			if (emailPOList != null) {
				for (int i = 0; i < emailPOList.size(); i++) {
					ParseObject tempEmailPO = emailPOList.get(i);
					Email tempEmail = globalVariable
							.convertPOtoEmail(tempEmailPO);
					emailList.add(tempEmail);
				}
				globalVariable.setEmailList(emailList);
			}
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
				emailLV.onRefreshComplete();
				if(getActivity() == null)
				{
					System.out.println(">>>>>>>activity null");
				}
				adapter = new EmailsAdapter(getActivity(), emailList);
				emailLV.setAdapter(adapter);

				emailLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						System.out.println(">>>>email position  : " + position);
						Email tempEmail = emailList.get(position - 1);
						if (!tempEmail.isEmailRead()) {

							tempEmail.setEmailRead(true);
							emailList.set(position - 1, tempEmail);

							MarkEmailAsReadAsynTask asynTask = new MarkEmailAsReadAsynTask();
							asynTask.execute(tempEmail.getObjectID());
						}
						nextIntent = new Intent(getActivity(),
								DisplayEmailActivity.class);
						nextIntent.putExtra("position", position - 1);
						startActivity(nextIntent);
					}
				});
			}
			progressBar.setVisibility(View.INVISIBLE);
		}

	}// Asyn

	private class MarkEmailAsReadAsynTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {

			ParseObject emailPO = null;
			try {
				emailPO = query.get(params[0]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (emailPO != null) {
				emailPO.put("isMailRead", true);
				emailPO.saveEventually(new SaveCallback() {

					@Override
					public void done(ParseException arg0) {
						Toast.makeText(getActivity(), "marked as read",
								Toast.LENGTH_SHORT).show();

					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void emailPOList) {
			super.onPostExecute(emailPOList);

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		new FetchEmailsAsynTask().execute(true);
	}
}
