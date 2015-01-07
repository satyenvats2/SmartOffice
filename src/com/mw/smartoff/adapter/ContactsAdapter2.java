package com.mw.smartoff.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoff.extras.CharacterDrawable2;
import com.mw.smartoff.model.User;
import com.mw.smartoffice.R;

@SuppressLint("DefaultLocale")
public class ContactsAdapter2 extends BaseAdapter {

	Context context;
	List<User> listPU;
	LayoutInflater inflater;

	HashMap<String, Integer> myMap;

	SharedPreferences sharedPreferences;
	Editor editor;

	public ContactsAdapter2(Context context, List<User> listPU) {
		super();
		this.context = context;
		this.listPU = listPU;
		String[] alphabets = context.getResources().getStringArray(
				R.array.alphabets);
		int[] hexCodes = context.getResources().getIntArray(R.array.hex_codes);
		myMap = new HashMap<String, Integer>();
		for (int i = 0; i < alphabets.length; i++) {
			myMap.put(alphabets[i], hexCodes[i]);
		}
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());
		editor = sharedPreferences.edit();
	}

	public void swapData(List<User> listPU) {
		this.listPU = listPU;
	}
	
	static class ViewHolder {
		protected ImageView senderIV;
		protected TextView nameTV;
		protected ImageView unreadMsgsIV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.contact_list_element,
					parent, false);

			viewHolder.senderIV = (ImageView) convertView
					.findViewById(R.id.sender_IV);
			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.name_TV);
			viewHolder.unreadMsgsIV = (ImageView) convertView
					.findViewById(R.id.unread_msgs_IV);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		User tempPU = listPU.get(position);
		CharacterDrawable2 drawable = new CharacterDrawable2(
				String.valueOf(tempPU.getUsername().toUpperCase().charAt(0)),
				myMap.get(tempPU.getUsername().toUpperCase().charAt(0) + ""));

		viewHolder.senderIV.setImageDrawable(drawable);

		viewHolder.nameTV.setText((String) tempPU.getName());
		if (sharedPreferences.getInt(tempPU.getObjectId(), 0) > 0) {
			CharacterDrawable2 drawable2 = new CharacterDrawable2(
					Integer.toString(sharedPreferences.getInt(
							tempPU.getObjectId(), 0)), 0xFFFF0000);
			viewHolder.unreadMsgsIV.setVisibility(View.VISIBLE);
			viewHolder.unreadMsgsIV.setImageDrawable(drawable2);
		} else {
			viewHolder.unreadMsgsIV.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return listPU.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
