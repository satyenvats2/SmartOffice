package com.mw.smartoff.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class UserAutoCompleteAdapter extends BaseAdapter implements Filterable {

	private Context context;
	List<ParseUser> mainList;
	List<ParseUser> origList;
	LayoutInflater inflater;
	private TempFilter filter;

	// Constructor
	public UserAutoCompleteAdapter(Context context, List<ParseUser> tempList) {
		this.context = context;
		this.mainList = tempList;
		origList = this.mainList;
		filter = new TempFilter();
	}

	static class ViewHolder {
		protected TextView usernameTV;
		protected TextView userEmailTV;
		protected ImageView profilePicIV;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_user_auto_complete,
					parent, false);
			viewHolder.profilePicIV = (ImageView) convertView
					.findViewById(R.id.profile_pic_IV);
			viewHolder.usernameTV = (TextView) convertView
					.findViewById(R.id.username_TV);
			viewHolder.userEmailTV = (TextView) convertView
					.findViewById(R.id.user_email_TV);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.usernameTV.setText(mainList.get(position).getUsername());
		viewHolder.userEmailTV.setText(mainList.get(position).getEmail());
//		viewHolder.usernameTV.setText(mainList.get(position).getUsername());
		
		return convertView;
	}

	@Override
	public int getCount() {
		if (mainList != null)
			return mainList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return mainList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// return tempList.get(arg0).getID();
		return 0;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	private class TempFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {
			System.out.println("performFiltering");
			FilterResults oReturn = new FilterResults();
			ArrayList<ParseUser> results = new ArrayList<ParseUser>();

			// next 2 lines are probably not important
			if (origList == null)
				origList = mainList;

			if (charSequence != null) {
				if (origList != null && origList.size() > 0) {
					for (ParseUser g : origList) {
						if (g.getUsername().contains(charSequence))
							results.add(g);
					}
				}
				oReturn.values = results;
			}
			// the return is sent to next function as argument
			return oReturn;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			System.out.println("publishResults");
			mainList = (ArrayList<ParseUser>) results.values;
			notifyDataSetChanged();
		}
	}

}
