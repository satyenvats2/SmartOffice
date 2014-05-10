package com.mw.smartoff.model;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

	private static final long serialVersionUID = 3602199814947009580L;

	String objectID;

	ParseUser fromPU;
	ParseUser toPU;
	
	String message;
    Date date;

	public Message(String objectID, ParseUser fromPU, ParseUser toPU,
			String message, Date date) {
		super();
		this.objectID = objectID;
		this.fromPU = fromPU;
		this.toPU = toPU;
		this.message = message;
        this.date = date;
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


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
