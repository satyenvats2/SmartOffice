package com.mw.smartoff.model;

public class NavDrawerItem {

	private String title;
	private int icon;
	
//	private String smallText = "0";
//	private boolean isSmallTextVisible = false;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
//	public NavDrawerItem(String title, int icon, boolean isSmallTextVisible, String smallText){
//		this.title = title;
//		this.icon = icon;
//		this.isSmallTextVisible = isSmallTextVisible;
//		this.smallText = smallText;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

}
