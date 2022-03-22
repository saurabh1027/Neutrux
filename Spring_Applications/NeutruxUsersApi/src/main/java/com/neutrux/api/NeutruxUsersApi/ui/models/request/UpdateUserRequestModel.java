package com.neutrux.api.NeutruxUsersApi.ui.models.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserRequestModel {

	@NotNull(message = "Firstname cannot be null!")
    @Size(min = 2, message = "Firstname must not be less than 2 characters!")
	private String firstname;
	
	@NotNull(message = "Lastname cannot be null!")
    @Size(min = 2, message = "Lastname must not be less than 2 characters!")
	private String lastname;

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
	
}
