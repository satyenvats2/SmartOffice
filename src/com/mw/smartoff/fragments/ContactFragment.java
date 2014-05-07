package com.mw.smartoff.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.DisplayMessagesActivity;
import com.mw.smartoff.adapter.ContactsAdapter;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

import java.util.List;

public class ContactFragment extends Fragment {
	TextView welcomeDashTV;
	ListView contactLV;
//	ProgressBar progressBar;

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
		asynTask.execute(true);
	}

	private void findThings() {
		contactLV = (ListView) getActivity().findViewById(R.id.contacts_LV);
//		progressBar = (ProgressBar) getActivity()
//				.findViewById(R.id.progressBar);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new UserDAO(getActivity());
	}

	private class FetchAllUsersAsynTask extends
			AsyncTask<Boolean, Void, List<ParseUser>> {

		@Override
		protected List<ParseUser> doInBackground(Boolean... params) {
			if (globalVariable.getUserList() != null && params[0]){
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
			globalVariable.setUserList(userList);
			return userList;
		}

		@Override
		protected void onPostExecute(List<ParseUser> usersListPOForDBUpdate) {
			super.onPostExecute(usersListPOForDBUpdate);
//			progressBar.setVisibility(View.INVISIBLE);

			List<ParseUser> usersListPO = globalVariable.getUserList();
			if (usersListPO != null && usersListPO.size() > 0) {
				adapter = new ContactsAdapter(getActivity(), usersListPO);
				contactLV.setAdapter(adapter);

				contactLV.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
//						Toast.makeText(getActivity(),
//								"position  : " + position, Toast.LENGTH_SHORT)
//								.show();
						nextIntent = new Intent(getActivity(),
								DisplayMessagesActivity.class);
						nextIntent.putExtra("position", position);
						startActivity(nextIntent);
					}
				});
			}
		}

	}

}
