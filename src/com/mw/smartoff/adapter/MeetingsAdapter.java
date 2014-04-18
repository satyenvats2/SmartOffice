package com.mw.smartoff.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mw.smartoff.model.Meeting;
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
		protected TextView subjectTV;
		protected TextView descriptionTV;
		protected TextView dateTV;
		protected TextView locationTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.meeting_list_element, parent,
					false);

			viewHolder.subjectTV = (TextView) convertView
					.findViewById(R.id.subject_TV);
			viewHolder.descriptionTV = (TextView) convertView
					.findViewById(R.id.description_TV);
			viewHolder.dateTV = (TextView) convertView
					.findViewById(R.id.date_TV);
			viewHolder.locationTV = (TextView) convertView
					.findViewById(R.id.location_TV);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Meeting temp = meetingList.get(position);
		viewHolder.subjectTV.setText(temp.getSubject());
		viewHolder.descriptionTV.setText(temp.getContent());
		viewHolder.dateTV.setText(temp.getStartTime().toString());
		viewHolder.locationTV.setText(temp.getLocation());

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
