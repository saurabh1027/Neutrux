package com.neutrux.api.NeutruxUsersApi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.neutrux.api.NeutruxUsersApi.shared.UserDto;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDto) throws Exception;
    UserDto getUserByUserId(String userId);
}