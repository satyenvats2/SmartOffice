package com.mw.smartoff.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 3004248537257133681L;

	String email;
	String username;

	public User(String email, String username) {
		super();
		this.email = email;
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
