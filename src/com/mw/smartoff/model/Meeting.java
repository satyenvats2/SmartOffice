package com.mw.smartoff.model;

import java.io.Serializable;
import java.util.Date;

public class Meeting implements Serializable {

	private static final long serialVersionUID = 3194600827024763515L;

	String ID;

	User from;
	
	String subject;
	String description;
	String location;
	Date startTime;

	boolean hasBeenResponsedTo;
	boolean currentResponse;

	public Meeting(String ID, User from, String subject, String description,
			String location, Date startTime) {
		super();
		this.from = from;
		this.ID = ID;
		this.subject = subject;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public boolean isHasBeenResponsedTo() {
		return hasBeenResponsedTo;
	}

	public void setHasBeenResponsedTo(boolean hasBeenResponsedTo) {
		this.hasBeenResponsedTo = hasBeenResponsedTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCurrentResponse() {
		return currentResponse;
	}

	public void setCurrentResponse(boolean currentResponse) {
		this.currentResponse = currentResponse;
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
