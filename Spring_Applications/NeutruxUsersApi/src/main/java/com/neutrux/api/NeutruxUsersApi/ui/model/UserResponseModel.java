package com.neutrux.api.NeutruxUsersApi.ui.model;

import java.io.Serializable;

public class UserResponseModel implements Serializable {

    //Attributes
    private static final long serialVersionUID = 1284310607042989889L;
    private String firstname;
    private String lastname;
    private String email;
    private String userId;

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
}
