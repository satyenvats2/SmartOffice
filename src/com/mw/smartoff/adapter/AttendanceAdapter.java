package com.mw.smartoff.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mw.smartoff.extras.GlobalVariable;
import com.mw.smartoffice.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

@SuppressLint("DefaultLocale")
public class AttendanceAdapter extends BaseAdapter implements Filterable {

	Context context;
	List<ParseUser> mainListPU;
	List<ParseUser> origListPU;
	LayoutInflater inflater;

	GlobalVariable globalVariable;
	List<ParseObject> attendanceListPO;

	private TempFilter filter;

	public AttendanceAdapter(Context context, List<ParseUser> listPU) {
		super();
		this.context = context;
		this.mainListPU = listPU;

		globalVariable = (GlobalVariable) context.getApplicationContext();
		attendanceListPO = globalVariable.getAttendancePOList();

		origListPU = this.mainListPU;
		filter = new TempFilter();
	}

	public void swapData() {
		// this.listPU = listPU;
		attendanceListPO = globalVariable.getAttendancePOList();
	}

	static class ViewHolder {
		protected TextView nameTV;
		protected TextView statusTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		System.out.println("getview");
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.attendance_element, parent,
					false);

			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.username_TV);
			viewHolder.statusTV = (TextView) convertView
					.findViewById(R.id.status_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ParseObject tempPO = mainListPU.get(position);
		for (int i = 0; i < attendanceListPO.size(); i++) {
			ParseObject tempAttendancePO = attendanceListPO.get(i);
			if (tempAttendancePO.getParseUser("from").getObjectId()
					.equals(tempPO.getObjectId())) {
				viewHolder.statusTV.setText("present.");
				viewHolder.statusTV.setTextColor(Color.parseColor("#0000FF"));
			} else {
				viewHolder.statusTV.setText("not present.");
				viewHolder.statusTV.setTextColor(Color.parseColor("#FF0000"));
			}
		}

		viewHolder.nameTV.setText(tempPO.getString("name") + " is ");
		return convertView;
	}

	@Override
	public int getCount() {
		return mainListPU.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
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
			if (origListPU == null)
				origListPU = mainListPU;

			if (charSequence != null) {
				if (origListPU != null && origListPU.size() > 0) {
					for (ParseUser g : origListPU) {
						if (g.getString("name").contains(charSequence)) {
							results.add(g);
						}
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
			mainListPU = (ArrayList<ParseUser>) results.values;
			notifyDataSetChanged();
		}
	}
}
