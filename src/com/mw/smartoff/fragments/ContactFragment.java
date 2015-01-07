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
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.services.ContactService;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

public class ContactFragment extends Fragment {
	TextView welcomeDashTV;
	ListView contactLV;
	ProgressBar progressBar;

	UserDAO dao;

	GlobalVariable globalVariable;
	List<ParseUser> usersListPU;
	ContactsAdapter adapter;

	Intent nextIntent;
	Intent serviceIntent;

	SharedPreferences sharedPreferences;
	Editor editor;

	private BroadcastReceiver contactListReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			System.out.println(">>>>>>>>>>>suyccesese");
			progressBar.setVisibility(View.INVISIBLE);

			usersListPU = globalVariable.getUserList();
			if (usersListPU != null && usersListPU.size() > 0) {
				if (adapter == null) {
					adapter = new ContactsAdapter(getActivity(), usersListPU);
					contactLV.setAdapter(adapter);
				} else {
					adapter.swapData(usersListPU);
					adapter.notifyDataSetChanged();
				}
				progressBar.setVisibility(View.INVISIBLE);
			}
			// Toast.makeText(context, "ContactFragment broadcast response",
			// Toast.LENGTH_SHORT).show();
		}
	};

	private BroadcastReceiver unreadMessagesReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.notifyDataSetChanged();
		}
	};

	private void findThings() {
		contactLV = (ListView) getActivity().findViewById(R.id.contacts_LV);
		progressBar = (ProgressBar) getActivity()
				.findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		usersListPU = globalVariable.getUserList();

		dao = new UserDAO(getActivity());

		serviceIntent = new Intent(getActivity(), ContactService.class);

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		editor = sharedPreferences.edit();

		// if users were not there in preferences, then usersListPO would be
		// null & cause problem in adapter
		if (usersListPU != null) {
			adapter = new ContactsAdapter(getActivity(), usersListPU);
			contactLV.setAdapter(adapter);
			progressBar.setVisibility(View.INVISIBLE);
		}
		contactLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				System.out.println("helloo");
				nextIntent = new Intent(getActivity(),
						DisplayMessagesActivity.class);
				globalVariable.setChatPerson(usersListPU.get(position));
				
				// What are next 2 lines for??
				editor.putInt(usersListPU.get(position).getObjectId(), 0);
				editor.commit();
				
				adapter.notifyDataSetChanged();
				// nextIntent.putExtra("position", position);
				startActivity(nextIntent);
			}
		});
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

		getActivity().startService(serviceIntent);
		// FetchAllUsersAsynTask asynTask = new FetchAllUsersAsynTask();
		// asynTask.execute("Hello World");
	}

	private class FetchAllUsersAsynTask extends
			AsyncTask<String, Void, List<ParseUser>> {

		@Override
		protected List<ParseUser> doInBackground(String... params) {
			if (globalVariable.getUserList() != null
					&& globalVariable.getUserList().size() > 0) {
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
			progressBar.setVisibility(View.INVISIBLE);

			final List<ParseUser> usersListPU = globalVariable.getUserList();
			if (usersListPU != null && usersListPU.size() > 0) {
				adapter = new ContactsAdapter(getActivity(), usersListPU);
				contactLV.setAdapter(adapter);

				contactLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						System.out.println("helloo");
						nextIntent = new Intent(getActivity(),
								DisplayMessagesActivity.class);
						globalVariable.setChatPerson(usersListPU.get(position));
						editor.putInt(usersListPU.get(position).getObjectId(),
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
				contactListReceiver);
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				unreadMessagesReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Toast.makeText(getActivity(), "onRedsume",
		// Toast.LENGTH_SHORT).show();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				contactListReceiver, new IntentFilter("contacts_count"));
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				unreadMessagesReceiver,
				new IntentFilter("unread_messages_from_various_users_count"));
	}
}
