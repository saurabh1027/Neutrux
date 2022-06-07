package com.neutrux.api.NeutruxBlogSearchApi.shared;

import java.io.Serializable;

public class UserDto implements Serializable {

	private static final long serialVersionUID = -191925623708124237L;
	private String userId;
	private String firstname;
	private String lastname;
	private String email;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
