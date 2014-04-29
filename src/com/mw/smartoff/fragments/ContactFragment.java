package com.mw.smartoff.fragments;

import java.util.ArrayList;
import java.util.List;

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

import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.MessagesActivity;
import com.mw.smartoff.adapter.ContactsAdapter;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.Parse;
import com.parse.ParseUser;

public class ContactFragment extends Fragment {
	TextView welcomeDashTV;
	ListView contactLV;
	UserDAO dao;
	GlobalVariable globalVariable;
	ContactsAdapter adapter;
	Intent nextIntent;

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
		asynTask.execute(new String[] { "Hello World" });
	}

	private void findThings() {
		contactLV = (ListView) getActivity().findViewById(R.id.contacts_LV);

	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new UserDAO(getActivity());
	}

	private class FetchAllUsersAsynTask extends
			AsyncTask<String, Void, List<ParseUser>> {

		@Override
		protected List<ParseUser> doInBackground(String... params) {

			List<ParseUser> userList = dao.getAllUsers();

			for (int i=0; i<userList.size();i++) {
				if (userList.get(i).getObjectId().equals(
						ParseUser.getCurrentUser().getObjectId())) {
					userList.remove(i);
					break;
				}
			}
			globalVariable.setUserList(userList);
			return userList;
		}

		@Override
		protected void onPostExecute(final List<ParseUser> usersList) {
			super.onPostExecute(usersList);
			if (usersList != null && usersList.size() > 0) {
				adapter = new ContactsAdapter(getActivity(), usersList);
				contactLV.setAdapter(adapter);

				contactLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Toast.makeText(getActivity(),
								"position  : " + position, Toast.LENGTH_SHORT)
								.show();
						nextIntent = new Intent(getActivity(),
								MessagesActivity.class);
						nextIntent.putExtra("position", position);
						startActivity(nextIntent);
					}
				});
			}
		}

	}// Asyn

}
