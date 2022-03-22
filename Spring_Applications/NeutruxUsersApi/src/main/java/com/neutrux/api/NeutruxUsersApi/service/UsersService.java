package com.neutrux.api.NeutruxUsersApi.service;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.neutrux.api.NeutruxUsersApi.shared.UserDto;

public interface UsersService extends UserDetailsService {
	
	Set<UserDto> getUsers(int pageNumber, int pageLimit); 
	
    UserDto createUser(UserDto userDetails) throws Exception;
    
    UserDto getUserByUserId(String userId);
    
    UserDto updateUserByUserId(UserDto newUserDetails);
    
    void deleteUserByUserId(String userId);
    
}