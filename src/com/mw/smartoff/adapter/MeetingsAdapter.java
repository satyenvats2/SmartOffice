package com.mw.smartoff.adapter;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoff.model.Meeting;
import com.mw.smartoff.services.CharacterDrawable;
import com.mw.smartoffice.R;

public class MeetingsAdapter extends BaseAdapter {

	Context context;
	List<Meeting> meetingList;
	LayoutInflater inflater;

	Date todayDate;

	public MeetingsAdapter(Context context, List<Meeting> meetingList) {
		super();
		this.context = context;
		this.meetingList = meetingList;
		todayDate = new Date();
	}

	static class ViewHolder {
		protected ImageView profilePicIV;
		protected TextView subjectTV;
		protected TextView senderUsernameTV;
		protected TextView dateTV;
		protected TextView locationTV;
		protected ImageView statusDotIV;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.meeting_list_element,
					parent, false);

			viewHolder.profilePicIV = (ImageView) convertView
					.findViewById(R.id.sender_IV);
			viewHolder.subjectTV = (TextView) convertView
					.findViewById(R.id.subject_TV);
			viewHolder.senderUsernameTV = (TextView) convertView
					.findViewById(R.id.description_TV);
			viewHolder.dateTV = (TextView) convertView
					.findViewById(R.id.date_TV);
//			viewHolder.locationTV = (TextView) convertView
//					.findViewById(R.id.location_TV);
			viewHolder.statusDotIV = (ImageView) convertView
					.findViewById(R.id.status_dot_IV);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Meeting tempMeeting = meetingList.get(position);
		CharacterDrawable drawable = new CharacterDrawable(tempMeeting
				.getFrom().getUsername().toUpperCase(Locale.getDefault()).charAt(0), 0xFF805781);
		viewHolder.profilePicIV.setImageDrawable(drawable);
		
		viewHolder.subjectTV.setText(tempMeeting.getSubject());
		viewHolder.senderUsernameTV.setText(tempMeeting.getFrom().getName());
		viewHolder.dateTV.setText(tempMeeting.getStartTime().toString());
		if (tempMeeting.isHasBeenResponsedTo())
			if (tempMeeting.isCurrentResponse())
				viewHolder.statusDotIV.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.dot_green));
			else
				viewHolder.statusDotIV.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.dot_red));
		else
			viewHolder.statusDotIV.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.dot_blue));
		return convertView;
	}

	@Override
	public int getCount() {
		return meetingList.size();
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
