package com.neutrux.api.NeutruxAuthenticationApi.service.implementation;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxAuthenticationApi.data.UserEntity;
import com.neutrux.api.NeutruxAuthenticationApi.data.UsersRepository;
import com.neutrux.api.NeutruxAuthenticationApi.service.UsersService;
import com.neutrux.api.NeutruxAuthenticationApi.shared.UserDto;

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

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = usersRepository.findByEmail(email);
		
		if(userEntity==null) {
			throw new UsernameNotFoundException(email+" not found!");
		}
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		
		String userId = encryptUserId(userEntity.getId());
		userDto.setUserId(userId);
		
		return userDto;
	}
	
	//Add some logic to avoid exposing database-userId
    //In Users API as well
    private String encryptUserId(long id) {
        return (id * 673926356)+"";
    }
	
}