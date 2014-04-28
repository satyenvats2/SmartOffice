package com.mw.smartoff.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mw.smartoff.DAO.EmailDAO;
import com.mw.smartoff.DAO.UserDAO;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

public class ContactFragment extends Fragment {
	TextView welcomeDashTV;
    ListView contactLV;
UserDAO dao;
    GlobalVariable globalVariable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contact_fragment, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
        findThings();
        initThings();
		ParseUser currentUser = ParseUser.getCurrentUser();
		welcomeDashTV.setText("Welcome : " + currentUser.getEmail());
	}

    private void findThings() {
        contactLV = (ListView) getActivity().findViewById(R.id.contacts_LV);
//        notifyEmailTV = (TextView) getActivity().findViewById(
//                R.id.notify_email_TV);
    }

    private void initThings() {
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        dao = new UserDAO(getActivity());
    }


}

