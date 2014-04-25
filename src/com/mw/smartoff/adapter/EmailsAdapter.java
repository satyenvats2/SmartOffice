package com.mw.smartoff.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.mw.smartoff.services.CharacterDrawable;
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

		CharacterDrawable drawable = new CharacterDrawable(tempEmail.getFrom()
				.getUsername().toUpperCase().charAt(0), 0xFF805781);
		viewHolder.senderIV.setImageDrawable(drawable);

		viewHolder.nameTV.setText(tempEmail.getFrom().getUsername());
		if (!tempEmail.isEmailRead()) 
			viewHolder.nameTV.setTypeface(null, Typeface.BOLD);
		viewHolder.subjectTV.setText(tempEmail.getSubject());
//		System.out.println(tempEmail.getCreatedAt());
		viewHolder.dateTV.setText(formatDate(tempEmail.getCreatedAt()));

		return convertView;
	}

	private String formatDate(Date date) {
		Date date1 = date;
		Date todayDate = new Date();
		boolean isYesterday = false;

		String COMPARE_FORMAT = "dd MMM yyyy zzzz";
		String NEW_FORMAT = null;

		SimpleDateFormat formatter = new SimpleDateFormat(COMPARE_FORMAT);

		String dateStr = formatter.format(date1);
		String todayDateStr = formatter.format(todayDate);

		Date date2 = null;
		Date todayDate2 = null;
		try {
			date2 = formatter.parse(dateStr);
			todayDate2 = formatter.parse(todayDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println( "\naa" + date2+"\nbb"+todayDate2);
		if (date2.compareTo(todayDate2) == 0)
			NEW_FORMAT = "HH:mm";
		else {
			if (getDate(date2, 1).compareTo(getDate(todayDate2, 1)) == 0)
				isYesterday = true;
			else
				NEW_FORMAT = "dd MMM";
		}
		SimpleDateFormat formatter2 = new SimpleDateFormat(NEW_FORMAT);
		if (isYesterday)
			return "Yesterday";
		else {
			return formatter2.format(date);

		}

	}

	Date getDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
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
