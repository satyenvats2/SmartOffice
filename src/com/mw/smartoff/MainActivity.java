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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.mw.smartoff.adapter.NavDrawerListAdapter;
import com.mw.smartoff.fragments.ContactFragment;
import com.mw.smartoff.fragments.EmailFragment;
import com.mw.smartoff.fragments.MeetingFragment;
import com.mw.smartoff.model.NavDrawerItem;
import com.mw.smartoff.services.CharacterDrawable;
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

	ListView leftDrawerLV;
	ArrayList<NavDrawerItem> navDrawerItemList;

    TextView usernameTV;

	GlobalVariable globalVariable;
	Intent nextIntent;

	Fragment fragment;
	String tag;
	
	FragmentManager fragmentManager;
	TypedArray navMenuIconsGreen;
	
	SharedPreferences sharedPreferences;
	Editor editor;

    // Hack for now
    Boolean flagNextIntent = false;

	private void findThings() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawerRLData = (RelativeLayout) findViewById(R.id.leftDrawer_RL);
		leftDrawerLV = (ListView) findViewById(R.id.leftDrawer_LV);
		usernameTV = (TextView) findViewById(R.id.username_TV);
	}

	private void initializeThings() {
		globalVariable = (GlobalVariable) getApplicationContext();
		navMenuIconsGreen = getResources().obtainTypedArray(
				R.array.nav_drawer_items_icons_green);
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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            String jsonData = extras.getString( "com.parse.Data" );
            return;
        }
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
				((ImageView)view.findViewById(R.id.icon_IV)).setImageResource(navMenuIconsGreen
				.getResourceId(position, -1));
				displayView(position);
			}
		});

        String username = (String) ParseUser.getCurrentUser().get("name");
        usernameTV.setText(username);

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

//    @Override
//    public void onRestart(){
//
//        super.onRestart();
//        Boolean pauseStatus = sharedPreferences.getBoolean("appPauseStatus", false);
//
//        if (pauseStatus){
//            Intent nextIntent = new Intent(this, VerifyPinActivity.class);
//            startActivity(nextIntent);
//        }
//    }
//
//    @Override
//    public void onStop(){
//
//        super.onStop();
//        if (!flagNextIntent){
//            editor.putBoolean("appPauseStatus", true);
//            editor.commit();
//        }
//    }
	

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

        // Satyen: we are removing PIN here
		editor.remove("pin");
		editor.commit();
		
		// Satyen: unsubscribing to channels
        Set<String> setOfAllSubscriptions = PushService.getSubscriptions(this);
        System.out.println(">>>>>>> Channels before clearing - " + setOfAllSubscriptions.toString());
        for (String setOfAllSubscription : setOfAllSubscriptions) {
            System.out.println(">>>>>>> MainActivity::onLogOut() - " + setOfAllSubscription);
            PushService.unsubscribe(this, setOfAllSubscription);
        }
        setOfAllSubscriptions = PushService.getSubscriptions(this);
        System.out.println(">>>>>>> Channels after cleared - " + setOfAllSubscriptions.toString());
        ParseUser.logOut();
        finish();

        nextIntent = new Intent(this, LoginActivity.class);
        startActivity(nextIntent);
	}

	public void onEmail(View view) {
        flagNextIntent = true;
		nextIntent = new Intent(this, CreateEmailActivity.class);
		startActivity(nextIntent);
	}

	public void onMeeting(View view) {
        flagNextIntent = true;
		nextIntent = new Intent(this, CreateMeetingActivity.class);
		startActivity(nextIntent);
	}
}
