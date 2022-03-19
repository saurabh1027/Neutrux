package com.neutrux.api.NeutruxUsersApi.ui.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neutrux.api.NeutruxUsersApi.service.UsersService;
import com.neutrux.api.NeutruxUsersApi.shared.UserDto;
import com.neutrux.api.NeutruxUsersApi.ui.model.request.CreateUserRequestModel;
import com.neutrux.api.NeutruxUsersApi.ui.model.response.UserResponseModel;

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
            @Valid @RequestBody CreateUserRequestModel createUserRequestModel) throws Exception{
    	ModelMapper modelMapper = new ModelMapper();
    	UserDto createdUser = null;
    	UserDto userDto = null;
    	UserResponseModel userResponseModel = null;
    	
    	modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        userDto = modelMapper.map(createUserRequestModel, UserDto.class);
        
    	createdUser = usersService.createUser(userDto);

        userResponseModel =
                modelMapper.map(createdUser, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseModel);
    }
    
    //Uncomment this line to allow access only for Admins & own user
    //@PreAuthorize("hasRole('ROLE_ADMIN') or principal == #userId")
//    @PreAuthorize("principal == #userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUserByUserId(@PathVariable("userId") String userId){
    	ModelMapper modelMapper = new ModelMapper();
    	UserResponseModel userResponseModel = null;
    	UserDto userDto = null;
    	
    	modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    	userDto = usersService.getUserByUserId(userId);
    	
    	userResponseModel = modelMapper.map(userDto, UserResponseModel.class);
    	return new ResponseEntity<UserResponseModel>(userResponseModel, HttpStatus.OK);
    }
    
    @PreAuthorize("principal == #userId")
    @PutMapping("/{userId}")
    public String updateUserByUserId(@PathVariable("userId") String userId, @RequestBody Object obj){
    	return "put user by userId-"+userId+" is called!";
    }
    
    @PreAuthorize("principal == #userId")
    @DeleteMapping("/{userId}")
    public String deleteUserByUserId(@PathVariable("userId") String userId){
    	return "delete user by userId-"+userId+" is called!";
    }

}
