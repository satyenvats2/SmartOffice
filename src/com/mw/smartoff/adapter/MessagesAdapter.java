package com.mw.smartoff.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mw.smartoff.model.Message;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

public class MessagesAdapter extends BaseAdapter {

	private Context context;
	private List<Message> msgsList;

	LayoutInflater inflater;

	public MessagesAdapter(Context context, List<Message> msgsList) {
		super();
		this.context = context;
		this.msgsList = msgsList;
	}

	static class ViewHolder {
		protected TextView messageTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.message_element, parent,
					false);

			viewHolder.messageTV = (TextView) convertView
					.findViewById(R.id.message_TV);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Message tempMessage = msgsList.get(position);
		viewHolder.messageTV.setText(tempMessage.getMessage());

		LayoutParams lp = (LayoutParams) viewHolder.messageTV.getLayoutParams();

		if (tempMessage.getFromPU().getObjectId()
				.equals(ParseUser.getCurrentUser().getObjectId())) {
			lp.gravity = Gravity.RIGHT;
			lp.setMargins(40, 0, 0, 0);
			viewHolder.messageTV
					.setBackgroundResource(R.drawable.speech_bubble_green_r);
		} else {
			lp.gravity = Gravity.LEFT;
			lp.setMargins(0, 0, 40, 0);

			viewHolder.messageTV
					.setBackgroundResource(R.drawable.speech_bubble_blue_l);
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return msgsList.size();
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
