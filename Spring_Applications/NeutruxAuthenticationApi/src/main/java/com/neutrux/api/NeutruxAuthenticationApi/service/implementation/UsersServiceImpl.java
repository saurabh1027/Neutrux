package com.neutrux.api.NeutruxAuthenticationApi.service.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxAuthenticationApi.data.UserEntity;
import com.neutrux.api.NeutruxAuthenticationApi.data.UsersRepository;
import com.neutrux.api.NeutruxAuthenticationApi.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService{
	
	private UsersRepository usersRepository;
	
	@Autowired
	public UsersServiceImpl(
		UsersRepository usersRepository
	) {
		this.usersRepository = usersRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = usersRepository.findByEmail(username);
		
		if(userEntity==null) {
			throw new UsernameNotFoundException(username+" not found!");
		}
		
		return new User(
			userEntity.getEmail(),
			userEntity.getEncryptedPassword(),
			true, true, true, true, 
			new ArrayList<>()
		);
	}
	
}