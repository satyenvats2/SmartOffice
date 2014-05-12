package com.mw.smartoff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.mw.smartoff.adapter.NavDrawerListAdapter;
import com.mw.smartoff.fragments.ContactFragment;
import com.mw.smartoff.fragments.EmailFragment;
import com.mw.smartoff.fragments.MeetingFragment;
import com.mw.smartoff.model.NavDrawerItem;
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.parse.PushService;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends FragmentActivity {

	DrawerLayout mDrawerLayout;
	RelativeLayout leftDrawerRLData;

	TextView usernameTV;
	TextView headerTitleTV;

	ListView leftDrawerLV;
	ArrayList<NavDrawerItem> navDrawerItemList;

	GlobalVariable globalVariable;
	Intent nextIntent;

	Fragment fragment;
	String tag;

	FragmentManager fragmentManager;

	SharedPreferences sharedPreferences;
	Editor editor;

	boolean gotoPinFlag;

	private void findThings() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawerRLData = (RelativeLayout) findViewById(R.id.leftDrawer_RL);
		leftDrawerLV = (ListView) findViewById(R.id.leftDrawer_LV);
		usernameTV = (TextView) findViewById(R.id.username_TV);
		headerTitleTV = (TextView) findViewById(R.id.header_title_TV);
	}

	private void initializeThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		editor = sharedPreferences.edit();
	}

	private void initialVisibilityOfViews() {

		usernameTV.setText(ParseUser.getCurrentUser().getString("name"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		findThings();
		initializeThings();
		initialVisibilityOfViews();
		drawerConfiguration();
		prepareLeftDrawerItems();

		NavDrawerListAdapter adapter = new NavDrawerListAdapter(this,
				navDrawerItemList);
		leftDrawerLV.setAdapter(adapter);

		leftDrawerLV.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("onItemClick");
				leftDrawerLV.clearChoices();
				leftDrawerLV.requestLayout();
				displayView(position);
			}
		});

		String username = (String) ParseUser.getCurrentUser().get("name");
		usernameTV.setText(username);

		Intent previousIntent = getIntent();
		if (previousIntent.hasExtra("type")) {
			displayView(previousIntent.getIntExtra("type", 0));
			GlobalVariable.TYPE_NOTIFICATION = previousIntent.getIntExtra(
					"type", 0);
			// put in global variable
		} else if (GlobalVariable.TYPE_NOTIFICATION != -1) {
			int temp = GlobalVariable.TYPE_NOTIFICATION;
			GlobalVariable.TYPE_NOTIFICATION = -1;
			displayView(temp);
			// reset global variable to -1

		} else if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		ParseAnalytics.trackAppOpened(getIntent());
	}

	private void drawerConfiguration() {
		mDrawerLayout.setScrimColor(getResources().getColor(
				android.R.color.transparent));

	}

	@Override
	public void onResume() {
		super.onResume();
		if (GlobalVariable.PIN == 0 && !GlobalVariable.FROM_VERIFY_PIN) {
			GlobalVariable.FROM_VERIFY_PIN = true;
			nextIntent = new Intent(this, VerifyPinActivity.class);
			startActivity(nextIntent);
		} else {
			GlobalVariable.FROM_VERIFY_PIN = false;
			GlobalVariable.PIN++;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Toast.makeText(this, "onPause MainA", Toast.LENGTH_SHORT).show();
		Gson gson = new Gson();
		if (globalVariable.getEmailList() != null) {
//			Toast.makeText(this, "iifff", Toast.LENGTH_SHORT).show();

			String json = gson.toJson(globalVariable.getEmailList());
			editor.putString("email_list", json);
		}
		else
		{
//			Toast.makeText(this, "elsing2", Toast.LENGTH_SHORT).show();
		}
		if (globalVariable.getMeetingList() != null) {
			String json = gson.toJson(globalVariable.getMeetingList());
			editor.putString("meeting_list", json);
		}
		if (globalVariable.getMeetingOwnList() != null) {
			String json = gson.toJson(globalVariable.getMeetingOwnList());
			editor.putString("meeting_own_list", json);
		}
		if (globalVariable.getMeetingPendingList() != null) {
			String json = gson.toJson(globalVariable.getMeetingPendingList());
			editor.putString("meeting_pending_list", json);
		}
		if (globalVariable.getMeetingPendingList() != null) {
			String json = gson.toJson(globalVariable.getMeetingPendingList());
			editor.putString("user_list", json);
		}
		editor.commit();
	}

	// @Override
	// public void onRestart() {
	// super.onRestart();
	// }

	@Override
	public void onStop() {

		super.onStop();
		if (GlobalVariable.PIN != 0)
			GlobalVariable.PIN--;
	}

	private void displayView(int position) {
		// update the main content by replacing fragments

		fragment = null;
		switch (position) {
		case 0:
			fragment = new EmailFragment();
			headerTitleTV.setText("Mailbox");
			tag = "EMAIL";
			break;
		case 1:
			fragment = new MeetingFragment();
			headerTitleTV.setText("Meetings");
			tag = "MEETING";
			break;
		case 2:
			fragment = new ContactFragment();
			headerTitleTV.setText("Select Contact");
			tag = "CONTACT";
			break;
		case 3:
			fragment = new ContactFragment();
			break;
		case 4:
			fragment = new ContactFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {

			fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_to_be_replaced, fragment, tag).commit();

			leftDrawerLV.setItemChecked(position, true);
			leftDrawerLV.setSelection(position);

			mDrawerLayout.closeDrawer(leftDrawerRLData);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	private void prepareLeftDrawerItems() {
		String[] navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items_title);
		TypedArray navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_items_icons);

		navDrawerItemList = new ArrayList<NavDrawerItem>();
		navDrawerItemList.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		navDrawerItemList.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItemList.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// navDrawerItemList.add(new NavDrawerItem(navMenuTitles[3],
		// navMenuIcons
		// .getResourceId(3, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

	}

	public void onOpenLeftDrawer(View view) {
		mDrawerLayout.openDrawer(leftDrawerRLData);
	}

	public void onLogOut(View view) {

		// Satyen: we are removing PIN here
		editor.remove("pin");
		editor.clear();
		editor.commit();

		// Satyen: unsubscribing to channels
		Set<String> setOfAllSubscriptions = PushService.getSubscriptions(this);
		System.out.println(">>>>>>> Channels before clearing - "
				+ setOfAllSubscriptions.toString());
		for (String setOfAllSubscription : setOfAllSubscriptions) {
			System.out.println(">>>>>>> MainActivity::onLogOut() - "
					+ setOfAllSubscription);
			PushService.unsubscribe(this, setOfAllSubscription);
		}
		setOfAllSubscriptions = PushService.getSubscriptions(this);
		System.out.println(">>>>>>> Channels after cleared - "
				+ setOfAllSubscriptions.toString());

		// Satyen: clear all lists
		globalVariable.setEmailList(null);
		globalVariable.setMeetingList(null);
		globalVariable.setMeetingOwnList(null);
		globalVariable.setMeetingPendingList(null);
		globalVariable.setUserList(null);
		GlobalVariable.resetOnLogout();
		ParseUser.logOut();

		nextIntent = new Intent(this, LoginActivity.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(nextIntent);
	}

}
