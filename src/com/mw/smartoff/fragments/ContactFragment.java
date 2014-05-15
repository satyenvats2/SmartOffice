package com.mw.smartoff.fragments;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mw.smartoff.DisplayMessagesActivity;
import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.adapter.ContactsAdapter;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

public class ContactFragment extends Fragment {
	TextView welcomeDashTV;
	ListView contactLV;
	ProgressBar progressBar;

	UserDAO dao;
	GlobalVariable globalVariable;
	ContactsAdapter adapter;
	Intent nextIntent;

	SharedPreferences sharedPreferences;
	Editor editor;

	private BroadcastReceiver unreadMessagesCounterReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			adapter.notifyDataSetChanged();
			System.out.println(">>>>>>>>>>>suyccesese");

		}
	};

	private void findThings() {
		contactLV = (ListView) getActivity().findViewById(R.id.contacts_LV);
		progressBar = (ProgressBar) getActivity()
				.findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new UserDAO(getActivity());

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		editor = sharedPreferences.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contact_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);
		findThings();
		initThings();

		FetchAllUsersAsynTask asynTask = new FetchAllUsersAsynTask();
		asynTask.execute(true);
	}

	private class FetchAllUsersAsynTask extends
			AsyncTask<Boolean, Void, List<ParseUser>> {

		@Override
		protected List<ParseUser> doInBackground(Boolean... params) {
			if (globalVariable.getUserList() != null && params[0]) {
				return null;
			}

			List<ParseUser> userList = dao.getAllUsers();

			for (int i = 0; i < userList.size(); i++) {
				if (userList.get(i).getObjectId()
						.equals(ParseUser.getCurrentUser().getObjectId())) {
					userList.remove(i);
					break;
				}
			}
			for (int i = 0; i < userList.size(); i++) {
				if (!sharedPreferences.contains(userList.get(i).getObjectId())) {
					editor.putInt(userList.get(i).getObjectId(), 0);
				}
			}
			editor.commit();
			globalVariable.setUserList(userList);
			return userList;
		}

		@Override
		protected void onPostExecute(List<ParseUser> usersListPOForDBUpdate) {
			super.onPostExecute(usersListPOForDBUpdate);
			// progressBar.setVisibility(View.INVISIBLE);

			final List<ParseUser> usersListPO = globalVariable.getUserList();
			if (usersListPO != null && usersListPO.size() > 0) {
				adapter = new ContactsAdapter(getActivity(), usersListPO);
				contactLV.setAdapter(adapter);

				contactLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						nextIntent = new Intent(getActivity(),
								DisplayMessagesActivity.class);
						globalVariable.setChatPerson(usersListPO.get(position));
						editor.putInt(usersListPO.get(position).getObjectId(),
								0);
						editor.commit();
						adapter.notifyDataSetChanged();
						// nextIntent.putExtra("position", position);
						startActivity(nextIntent);
					}
				});
			}
		}// onPostExecute()

	}// Asyn

	@Override
	public void onPause() {
		super.onPause();
		// Toast.makeText(getActivity(), "onPasue", Toast.LENGTH_SHORT).show();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				unreadMessagesCounterReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Toast.makeText(getActivity(), "onRedsume",
		// Toast.LENGTH_SHORT).show();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				unreadMessagesCounterReceiver,
				new IntentFilter("unread_messages_count"));
	}
}
