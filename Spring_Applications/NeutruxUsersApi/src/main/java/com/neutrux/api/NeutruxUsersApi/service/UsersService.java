package com.neutrux.api.NeutruxUsersApi.service;

import com.neutrux.api.NeutruxUsersApi.shared.UserDto;

public interface UsersService {
    UserDto createUser(UserDto userDto);
}