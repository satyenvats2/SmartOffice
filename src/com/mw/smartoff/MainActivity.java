package com.mw.smartoff;

import java.util.ArrayList;
import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mw.smartoff.adapter.NavDrawerListAdapter;
import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoff.fragments.AttendanceFragment;
import com.mw.smartoff.fragments.ContactFragment;
import com.mw.smartoff.fragments.EmailFragment;
import com.mw.smartoff.fragments.MeetingFragment;
import com.mw.smartoff.model.NavDrawerItem;
import com.mw.smartoffice.R;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.parse.PushService;

public class MainActivity extends FragmentActivity {
	private ActionBarDrawerToggle mDrawerToggle;

	DrawerLayout mDrawerLayout;
	RelativeLayout leftDrawerRLData;

	TextView usernameTV;

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

	NavDrawerListAdapter adapter;

	private CharSequence drawerTitle;

	private CharSequence appTitle;

	private void findThings() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawerRLData = (RelativeLayout) findViewById(R.id.leftDrawer_RL);
		leftDrawerLV = (ListView) findViewById(R.id.leftDrawer_LV);
		usernameTV = (TextView) findViewById(R.id.username_TV);
	}

	private void initializeThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		editor = sharedPreferences.edit();

		drawerTitle = getTitle();
		appTitle = getTitle();

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(drawerTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(appTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
	}

	private void initialVisibilityOfViews() {
		usernameTV.setText(ParseUser.getCurrentUser().getString("name"));
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");
		TextView yourTextView = (TextView) findViewById(titleId);
		yourTextView.setTypeface(Typeface.SERIF);

		findThings();
		initializeThings();
		initialVisibilityOfViews();
		drawerConfiguration();
		prepareLeftDrawerItems();

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		leftDrawerLV.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(this, navDrawerItemList);
		leftDrawerLV.setAdapter(adapter);

		// these 2 lines are used to show the small drawer button at the left in action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
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
		Toast.makeText(this, "onPause MainActivity", Toast.LENGTH_SHORT).show();
		Gson gson = new Gson();
		if (globalVariable.getEmailList() != null && globalVariable.getEmailList().size()>0) {
			String json = gson.toJson(globalVariable.getEmailList());
			editor.putString("email_list", json);
		} else {
		}
		if (globalVariable.getMeetingList() != null && globalVariable.getMeetingList().size()>0) {
			String json = gson.toJson(globalVariable.getMeetingList());
			editor.putString("meeting_list", json);
		}
		if (globalVariable.getMeetingOwnList() != null && globalVariable.getMeetingOwnList().size()>0) {
			String json = gson.toJson(globalVariable.getMeetingOwnList());
			editor.putString("meeting_own_list", json);
		}
		if (globalVariable.getMeetingPendingList() != null && globalVariable.getMeetingPendingList().size()>0) {
			String json = gson.toJson(globalVariable.getMeetingPendingList());
			editor.putString("meeting_pending_list", json);
		}
//		if (globalVariable.getUserList() != null && globalVariable.getUserList().size()>0) {
//			String json = gson.toJson(globalVariable.getUserList());
//			editor.putString("user_list", json);
//		}
		editor.commit();
	}

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
			tag = "EMAIL";
			break;
		case 1:
			fragment = new MeetingFragment();
			tag = "MEETING";
			break;
		case 2:
			fragment = new ContactFragment();
			tag = "CONTACT";
			break;
		case 3:
			fragment = new AttendanceFragment();
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
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(leftDrawerRLData);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		drawerTitle = title;
		getActionBar().setTitle(drawerTitle);
	}

	String[] navMenuTitles;

	private void prepareLeftDrawerItems() {
		navMenuTitles = getResources().getStringArray(
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
//		navDrawerItemList.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
//				.getResourceId(3, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

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
		globalVariable.resetOnLogout2();
		GlobalVariable.resetOnLogout();
		ParseUser.logOut();

		nextIntent = new Intent(this, LoginActivity.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(nextIntent);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
