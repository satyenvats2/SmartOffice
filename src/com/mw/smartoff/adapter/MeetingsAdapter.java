package com.mw.smartoff.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.mw.smartoff.services.GlobalVariable;
import com.mw.smartoffice.R;

public class MeetingsAdapter extends BaseAdapter {

	Context context;
	List<Meeting> meetingList;
	LayoutInflater inflater;

	Date todayDate;
	
	HashMap<String, Integer> myMap;
	
	GlobalVariable globalVariable;
	
	public MeetingsAdapter(Context context, List<Meeting> meetingList) {
		super();
		this.context = context;
		this.meetingList = meetingList;

		globalVariable = (GlobalVariable) context.getApplicationContext();
		
		todayDate = new Date();
		myMap = globalVariable.getMyMap();
		
	}

	static class ViewHolder {
		protected ImageView profilePicIV;
		protected TextView subjectTV;
		protected TextView senderUsernameTV;
		protected TextView dateTV;
		protected TextView locationTV;
        protected TextView statusTV;
		protected ImageView statusDotIV;

	}

	public void swapData(List<Meeting> meetingList) {
		this.meetingList = meetingList;
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
			viewHolder.statusTV = (TextView) convertView
					.findViewById(R.id.status_TV);
			viewHolder.statusDotIV = (ImageView) convertView
					.findViewById(R.id.status_dot_IV);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Meeting tempMeeting = meetingList.get(position);
		// toUpperCase(Locale.getDefault())
		CharacterDrawable drawable = new CharacterDrawable(tempMeeting
				.getFrom().getUsername().toUpperCase(Locale.getDefault()).charAt(0), myMap.get(tempMeeting.getFrom()
						.getUsername().toUpperCase(Locale.getDefault()).charAt(0)+""));
		viewHolder.profilePicIV.setImageDrawable(drawable);
		
		viewHolder.subjectTV.setText(tempMeeting.getSubject());
		viewHolder.senderUsernameTV.setText(tempMeeting.getFrom().getName());
		viewHolder.dateTV.setText(getDisplayDate(tempMeeting.getStartTime()));
		if (tempMeeting.isHasBeenResponsedTo())
			if (tempMeeting.isCurrentResponse()){
                viewHolder.statusTV.setText("Accepted");
                viewHolder.statusTV.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            }
                else {
                viewHolder.statusTV.setText("Rejected");
                viewHolder.statusTV.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
		else {
            viewHolder.statusTV.setText("Pending");
//            viewHolder.statusTV.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.statusTV.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }
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

    public String getDisplayDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, HH:mm");
        String stringDate = simpleDateFormat.format(date);
        return stringDate;
    }

}
