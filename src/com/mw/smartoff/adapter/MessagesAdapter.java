package com.mw.smartoff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mw.smartoff.model.Message;
import com.mw.smartoffice.R;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        protected RelativeLayout messageLL;
		protected TextView messageTV;
        protected TextView dateTV;
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

            viewHolder.messageLL = (RelativeLayout) convertView.findViewById(R.id.message_LL);
			viewHolder.messageTV = (TextView) convertView
					.findViewById(R.id.message_TV);
            viewHolder.dateTV = (TextView) convertView.findViewById(R.id.dateView_TV);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Message tempMessage = msgsList.get(position);
		viewHolder.messageTV.setText(tempMessage.getMessage());
        viewHolder.dateTV.setText(getDisplayDate(tempMessage.getDate()));

        RelativeLayout.LayoutParams lllp = (RelativeLayout.LayoutParams) viewHolder.messageLL.getLayoutParams();

		if (tempMessage.getFromPU().getObjectId()
				.equals(ParseUser.getCurrentUser().getObjectId())) {
            lllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lllp.setMargins(60, 0, 20, 0);
            viewHolder.messageLL.setBackgroundResource(R.drawable.speech_bubble_green_r);
		} else {
//            lllp.gravity = Gravity.LEFT;
            lllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lllp.setMargins(20, 0, 60, 0);
            viewHolder.messageLL.setBackgroundResource(R.drawable.speech_bubble_blue_l);
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

    public String getDisplayDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd MMM, hh:mm");
        String stringDate = simpleDateFormat.format(date);
        return stringDate;
    }

}
