package com.neutrux.api.NeutruxAuthenticationApi.ui.models.response;

import java.io.Serializable;

public class UserResponseModel implements Serializable {

	//Attributes
	private static final long serialVersionUID = -6467132656154946047L;
    private String firstname;
    private String lastname;
    private String email;
    private String userId;
    private long expiresIn;
    private String roles;

    //Getters & Setters
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public long getExpiresIn() {
    	return expiresIn;
    }
    
    public void setExpiresIn(long expiresIn) {
    	this.expiresIn = expiresIn;
    }
    
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "{"
				+ "\"firstname\":\""+ this.firstname +"\","
				+ "\"lastname\":\""+ this.lastname +"\","
				+ "\"email\":\""+ this.email +"\","
				+ "\"userId\":\""+ this.userId +"\","
				+ "\"expiresIn\":\""+ this.expiresIn +"\","
				+ "\"roles\":\""+ this.roles +"\""
				+ "}";
	}
	
}
