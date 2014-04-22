package com.mw.smartoff.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoff.model.Email;
import com.mw.smartoffice.R;

public class EmailsAdapter extends BaseAdapter {

	Context context;
	List<Email> emailList;
	LayoutInflater inflater;

	Date todayDate;

	public EmailsAdapter(Context context, List<Email> emailList) {
		super();
		this.context = context;
		this.emailList = emailList;
		todayDate = new Date();
	}

	public void swapData(List<Email> emailList) {
		this.emailList = emailList;
	}
	
	static class ViewHolder {
		protected ImageView senderIV;
		protected TextView nameTV;
		protected TextView subjectTV;
		protected TextView dateTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.email_list_element, parent,
					false);

			viewHolder.senderIV = (ImageView) convertView
					.findViewById(R.id.sender_IV);
			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.name_TV);
			viewHolder.subjectTV = (TextView) convertView
					.findViewById(R.id.subject_TV);
			viewHolder.dateTV = (TextView) convertView
					.findViewById(R.id.date_TV);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Email tempEmail = emailList.get(position);
		if (tempEmail == null)
			System.out.println("temp is null");
		else
			System.out.println("temp is not null");

		viewHolder.nameTV.setText(tempEmail.getFrom().getUsername());
		if (!tempEmail.isEmailRead()) {
			System.out.println("bolding");
			viewHolder.nameTV.setTypeface(null, Typeface.BOLD);
		}
		viewHolder.subjectTV.setText(tempEmail.getSubject());
		// viewHolder.dateTV.setText(temp.getCreatedAt().toString());
		viewHolder.dateTV.setText(formatDate(tempEmail.getCreatedAt()));

		return convertView;
	}

	private String formatDate(Date date) {
		String OLD_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

		SimpleDateFormat formatter = new SimpleDateFormat(OLD_FORMAT);

		Date tempDate = null;
		Date tempTodayDate = null;
		try {
			tempDate = formatter.parse(date.toString());
			tempTodayDate = formatter.parse(todayDate.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String NEW_FORMAT = null;
		if (tempDate.compareTo(tempTodayDate) == 0)
			NEW_FORMAT = "HH:mm";
		else
			NEW_FORMAT = "dd/MM";

		formatter.applyPattern(NEW_FORMAT);

		String newDateString = formatter.format(tempDate);

		return newDateString;
	}

	@Override
	public int getCount() {
		return emailList.size();
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
