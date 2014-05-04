package com.mw.smartoff.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 3004248537257133681L;

	String email;
	String username;
	String name;

	public User(String email, String username, String name) {
		super();
		this.email = email;
		this.username = username;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean equals(Object o) {
		User user = null;
		if (o instanceof User)
			user = (User) o;
		if (user != null && user.getEmail() == this.getEmail()
				&& user.getUsername() == this.getUsername())
			return true;
		else
			return false;
		// return super.equals(o);

	}

}
