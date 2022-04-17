package com.neutrux.api.NeutruxBlogsApi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.neutrux.api.NeutruxBlogsApi.shared.UserDto;

public interface UsersService extends UserDetailsService {
	
    UserDto getUserByUserId(String userId) throws NumberFormatException, Exception;
    
    
    String encryptUserId(long id);
    
    long decryptUserId(String userId) throws Exception;
    
}