package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;


import android.app.ProgressDialog;
//import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayEmailActivity;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.adapter.EmailsAdapter;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.CreateDialog;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseException;
import com.parse.ParseObject;
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

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	
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
//		progressDialog.show();
		FetchEmailsAsynTask asynTask = new FetchEmailsAsynTask();
		asynTask.execute(true);
        // Set a listener to be invoked when the list should be refreshed.
        emailLV.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                // Do work to refresh the list here.
//                new PullToRefreshDataTask().execute();
                new FetchEmailsAsynTask().execute(false);
            }
        });
	}

	private void findThings() {
		emailLV = (PullAndLoadListView) getActivity().findViewById(R.id.email_LV);
		notifyEmailTV = (TextView) getActivity().findViewById(
				R.id.notify_email_TV);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        if (globalVariable.getEmailList() != null){
            progressBar.setVisibility(View.INVISIBLE);
        }
		dao = new EmailDAO(getActivity());
//		createDialog = new CreateDialog(getActivity());
//		progressDialog = createDialog.createProgressDialog("Loading",
//				"Fetching Emails", true, null);
	}

	private class FetchEmailsAsynTask extends
			AsyncTask<Boolean, Void, List<ParseObject>> {
		// ParseUser user;
		@Override
		protected List<ParseObject> doInBackground(Boolean... params) {

            if (globalVariable.getEmailList() != null && params[0]){
                return null;
            }
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
                emailLV.onRefreshComplete();
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
            progressBar.setVisibility(View.INVISIBLE);
//			progressDialog.dismiss();
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

//    private class PullToRefreshDataTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            if (isCancelled()) {
//                return null;
//            }
//
//            // Simulates a background task
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
//
//            for (int i = 0; i < mAnimals.length; i++)
//                mListItems.addFirst(mAnimals[i]);
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            mListItems.addFirst("Added after pull to refresh");
//
//            // We need notify the adapter that the data have been changed
//            ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
//
//            // Call onLoadMoreComplete when the LoadMore task, has finished
//            ((PullAndLoadListView) getListView()).onRefreshComplete();
//
//            super.onPostExecute(result);
//        }
//
//        @Override
//        protected void onCancelled() {
//            // Notify the loading more operation has finished
//            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
//        }
//    }
}
