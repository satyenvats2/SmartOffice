package com.mw.smartoff.model;

import java.io.Serializable;
import java.util.Date;

public class Meeting implements Serializable {

	private static final long serialVersionUID = 3194600827024763515L;

	String subject;
	String description;
	String location;
	Date startTime;

	public Meeting(String subject, String description, String location,
			Date startTime) {
		super();
		this.subject = subject;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return description;
	}

	public void setContent(String content) {
		this.description = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
