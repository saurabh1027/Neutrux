package com.neutrux.api.NeutruxUsersApi.ui.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

    //Attributes
    @NotNull(message = "Firstname cannot be null!")
    @Size(min = 2, message = "Firstname must not be less than 2 characters!")
    private String firstname;

    @NotNull(message = "Lastname cannot be null!")
    @Size(min = 2, message = "Lastname must not be less than 2 characters!")
    private String lastname;

    @NotNull(message = "Email cannot be null!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotNull(message = "Password cannot be null!")
    @Size(min = 8, max = 16,
            message = "Password must be equal to or greater than 8 characters and less than 16 characters!")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
