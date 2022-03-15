package com.neutrux.api.NeutruxAuthenticationApi.ui.controller;

import com.neutrux.api.NeutruxAuthenticationApi.ui.model.AuthenticateUserRequestModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//import javax.validation.Valid;

@RestController
public class UsersController {

//    @PostMapping("/authenticate")
//    public String authenticateUser(
//            @Valid @RequestBody AuthenticateUserRequestModel authenticateUserRequestModel){
//        return "authenticate api called!";
//    }
    
    @GetMapping("/status")
    public String status(){
    	return "status api1 called!";
    }

}