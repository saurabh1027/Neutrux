package com.neutrux.api.NeutruxUsersApi.ui.controller;

import com.neutrux.api.NeutruxUsersApi.service.UsersService;
import com.neutrux.api.NeutruxUsersApi.shared.UserDto;
import com.neutrux.api.NeutruxUsersApi.ui.model.CreateUserRequestModel;
import com.neutrux.api.NeutruxUsersApi.ui.model.UserResponseModel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    private Environment environment;
    private UsersService usersService;

    @Autowired
    public UsersController(
            Environment environment,
            UsersService usersService
    ){
        this.environment = environment;
        this.usersService = usersService;
    }

    @GetMapping("/status")
    public String checkStatus(){
        return "Working on port: " + environment.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(
            @Valid @RequestBody CreateUserRequestModel createUserRequestModel){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(createUserRequestModel, UserDto.class);
        UserDto createdUser = usersService.createUser(userDto);

        UserResponseModel userResponseModel =
                modelMapper.map(createdUser, UserResponseModel.class);

        //Check out exception handling for, What if,
            //email already exists
            //some of the inputs are unacceptable
            //look for more exceptions
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseModel);
    }

}
