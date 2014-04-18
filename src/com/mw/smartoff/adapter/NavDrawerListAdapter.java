package com.mw.smartoff.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mw.smartoff.model.NavDrawerItem;
import com.mw.smartoffice.R;

public class NavDrawerListAdapter extends BaseAdapter {

	Context context;
	ArrayList<NavDrawerItem> navDrawerItemsList;
	LayoutInflater inflater;
	
	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItemsList) {
		super();
		this.context = context;
		this.navDrawerItemsList = navDrawerItemsList;
	}

	static class ViewHolder {
		protected ImageView iconIV;
		protected TextView titleTV;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.drawer_list_element, parent, false);
			viewHolder.titleTV = (TextView) convertView.findViewById(R.id.title_TV);
			viewHolder.iconIV = (ImageView) convertView.findViewById(R.id.icon_IV);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		NavDrawerItem temp = navDrawerItemsList.get(position);
		
		viewHolder.titleTV.setText(temp.getTitle());
		viewHolder.iconIV.setImageResource(temp.getIcon());
		
		return convertView;
	}

	
	@Override
	public int getCount() {
		return navDrawerItemsList.size();
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
