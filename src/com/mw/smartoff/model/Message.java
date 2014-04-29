package com.mw.smartoff.model;

import java.io.Serializable;

import com.parse.ParseUser;

public class Message implements Serializable {

	private static final long serialVersionUID = 3602199814947009580L;

	String objectID;

	ParseUser fromPU;
	ParseUser toPU;
	
	String message;

	public Message(String objectID, ParseUser fromPU, ParseUser toPU,
			String message) {
		super();
		this.objectID = objectID;
		this.fromPU = fromPU;
		this.toPU = toPU;
		this.message = message;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public ParseUser getFromPU() {
		return fromPU;
	}

	public void setFromPU(ParseUser fromPU) {
		this.fromPU = fromPU;
	}

	public ParseUser getToPU() {
		return toPU;
	}

	public void setToPU(ParseUser toPU) {
		this.toPU = toPU;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
