package com.mw.smartoff.fragments;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.mw.smartoff.DisplayEmailActivity;
import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.adapter.EmailsAdapter;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.model.Email;
import com.mw.smartoff.services.EmailService;
import com.mw.smartoffice.R;

//import android.app.Fragment;

public class EmailFragment extends Fragment {
	PullAndLoadListView emailLV;
	TextView notificationTV;
	RelativeLayout progressBarRL;

	GlobalVariable globalVariable;
	List<Email> emailList;
	EmailsAdapter adapter;

	EmailDAO dao;

	Intent serviceIntent;
	Intent nextIntent;

	private BroadcastReceiver emailReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			// String message = intent.getStringExtra("message");
			progressBarRL.setVisibility(View.GONE);
			
//			usersListPU = globalVariable.getUserList();
//			if (usersListPU != null && usersListPU.size() > 0) {
//				if (adapter == null) {
//					adapter = new ContactsAdapter(getActivity(), usersListPU);
//					contactLV.setAdapter(adapter);
//				} else {
//					adapter.swapData(usersListPU);
//					adapter.notifyDataSetChanged();
//				}
//				prog
			
			
//			
//			emailList = globalVariable.getEmailList();
//			if (emailList.size() < 1) {
//				notificationTV.setVisibility(View.VISIBLE);
//			}
//			adapter.swapData(emailList);
//			emailLV.onRefreshComplete();
//			adapter.notifyDataSetChanged();
			
			emailList = globalVariable.getEmailList();
			if (emailList != null && emailList.size() > 0) {
				if (adapter == null) {
					adapter = new EmailsAdapter(getActivity(), emailList);
					emailLV.setAdapter(adapter);
				} else {
					adapter.swapData(emailList);
					adapter.notifyDataSetChanged();
				}
				notificationTV.setVisibility(View.INVISIBLE);
			}
			
//			Toast.makeText(context, "EmailFrag broad response",
//					Toast.LENGTH_SHORT).show();
		}
	};

	private void findThings() {
		emailLV = (PullAndLoadListView) getActivity().findViewById(
				R.id.email_LV);
		notificationTV = (TextView) getActivity().findViewById(
				R.id.notify_email_TV);
		progressBarRL = (RelativeLayout) getActivity().findViewById(
				R.id.progressBar_RL);
	}

	private void initThings() {
		globalVariable = (GlobalVariable) getActivity().getApplicationContext();
		dao = new EmailDAO(getActivity());

		serviceIntent = new Intent(getActivity(), EmailService.class);
		nextIntent = new Intent(getActivity(), DisplayEmailActivity.class);

		emailList = globalVariable.getEmailList();
		if (emailList != null && emailList.size() > 0) {
			progressBarRL.setVisibility(View.GONE);
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

					adapter.notifyDataSetChanged();
					dao.markEmailAsRead(tempEmail.getObjectID());
				}

				nextIntent.putExtra("position", position - 1);
				startActivity(nextIntent);
			}
		});
	}

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
		// Set a listener to be invoked when the list should be refreshed.
		emailLV.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// Do work to refresh the list here.
				// new PullToRefreshDataTask().execute();
				getActivity().startService(serviceIntent);
			}
		});

		getActivity().startService(serviceIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				emailReceiver, new IntentFilter("new_email"));
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				emailReceiver);
	}
}
