package com.neutrux.api.NeutruxAuthenticationApi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.neutrux.api.NeutruxAuthenticationApi.shared.UserDto;

public interface UsersService extends UserDetailsService {
	UserDto getUserDetailsByEmail(String email);
}
