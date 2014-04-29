package com.mw.smartoff;

import java.util.ArrayList;
import java.util.Set;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

public class MainActivity extends FragmentActivity {

	DrawerLayout mDrawerLayout;
	RelativeLayout leftDrawerRLData;

	ListView leftDrawerLV;
	ArrayList<NavDrawerItem> navDrawerItemList;

	GlobalVariable globalVariable;
	Intent nextIntent;

	Fragment fragment;
	String tag;
	
	FragmentManager fragmentManager;
	TypedArray navMenuIconsGreen;
	
	SharedPreferences sharedPreferences;
	Editor editor;
	
	private void findThings() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawerRLData = (RelativeLayout) findViewById(R.id.leftDrawer_RL);
		leftDrawerLV = (ListView) findViewById(R.id.leftDrawer_LV);
	}

	private void initializeThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		navMenuIconsGreen = getResources().obtainTypedArray(
				R.array.nav_drawer_items_icons_green);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		editor = sharedPreferences.edit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		findThings();
		initializeThings();
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
				((ImageView)view.findViewById(R.id.icon_IV)).setImageResource(navMenuIconsGreen
				.getResourceId(position, -1));
				displayView(position);
			}
		});

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		ParseAnalytics.trackAppOpened(getIntent());
	}

	private void drawerConfiguration() {
		mDrawerLayout.setScrimColor(getResources().getColor(
				android.R.color.transparent));

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
//		navDrawerItemList.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
//				.getResourceId(3, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

	}

	public void onOpenLeftDrawer(View view) {
		mDrawerLayout.openDrawer(leftDrawerRLData);
	}

	public void onLogOut(View view) {
		editor.remove("pin");

		editor.commit();
		
		
        Set<String> setOfAllSubscriptions = PushService.getSubscriptions(this);
        System.out.println(">>>>>>> Channels before clearing - " + setOfAllSubscriptions.toString());
        for (String setOfAllSubscription : setOfAllSubscriptions) {
            System.out.println(">>>>>>> MainActivity::onLogOut() - " + setOfAllSubscription);
            PushService.unsubscribe(this, setOfAllSubscription);
        }
        setOfAllSubscriptions = PushService.getSubscriptions(this);
        System.out.println(">>>>>>> Channels after cleared - " + setOfAllSubscriptions.toString());
        ParseUser.logOut();finish();
	}

	// public void onComposeEmailOrMeeting(View view) {
	// Toast.makeText(this, "put compose functionality", Toast.LENGTH_SHORT)
	// .show();
	// // EmailFragment fragment =
	// // (EmailFragment)getFragmentManager().findFragmentByTag("EMAIL");
	// ContactFragment fragment = (ContactFragment) fragmentManager
	// .findFragmentByTag("DASHBOARD");
	//
	// if (fragment.isVisible())
	// Toast.makeText(this, "if", Toast.LENGTH_SHORT)
	// .show();
	// else
	// Toast.makeText(this, "else", Toast.LENGTH_SHORT)
	// .show();
	// }

	public void onEmail(View view) {
		nextIntent = new Intent(this, CreateEmailActivity.class);
		startActivity(nextIntent);
	}

	public void onMeeting(View view) {
		nextIntent = new Intent(this, CreateMeetingActivity.class);
		startActivity(nextIntent);
	}
}
