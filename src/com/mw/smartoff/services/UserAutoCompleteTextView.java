package com.mw.smartoff.services;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.parse.ParseUser;

public class UserAutoCompleteTextView extends AutoCompleteTextView {

	public UserAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** Returns the country name corresponding to the selected item */
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		ParseUser temp = (ParseUser) selectedItem;
		return temp.getUsername();
	}

	
	
	
}
