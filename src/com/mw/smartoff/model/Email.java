package com.mw.smartoff.model;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {

	private static final long serialVersionUID = 8129837454345331233L;

	String objectID;
	
	User from;
	
	String subject;
	String content;

	boolean isEmailRead;

	Date createdAt;
	

	public Email(String objectID, User from, String subject, String content, boolean isEmailRead,
			Date createdAt) {
		super();
		this.objectID = objectID;
		this.from = from;
		this.subject = subject;
		this.content = content;
		this.isEmailRead = isEmailRead;
		this.createdAt = createdAt;
	}

	
	public String getObjectID() {
		return objectID;
	}


	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}


	public User getFrom() {
		return from;
	}


	public void setFrom(User from) {
		this.from = from;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isEmailRead() {
		return isEmailRead;
	}

	public void setEmailRead(boolean isEmailRead) {
		this.isEmailRead = isEmailRead;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	
}
