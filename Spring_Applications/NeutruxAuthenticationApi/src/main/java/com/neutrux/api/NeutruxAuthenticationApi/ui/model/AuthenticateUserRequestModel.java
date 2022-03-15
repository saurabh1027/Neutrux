package com.neutrux.api.NeutruxAuthenticationApi.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthenticateUserRequestModel {

    //Attributes
    @NotNull(message = "Email cannot be null!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotNull(message = "Password cannot be null!")
    @Size(min = 8, max = 16,
            message = "Password must be equal to or greater than 8 characters and less than 16 characters!")
    private String password;

    //Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}