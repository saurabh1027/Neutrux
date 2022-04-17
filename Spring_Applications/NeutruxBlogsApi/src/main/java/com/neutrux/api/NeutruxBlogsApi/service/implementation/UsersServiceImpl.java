package com.neutrux.api.NeutruxBlogsApi.service.implementation;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxBlogsApi.repositories.UsersRepository;
import com.neutrux.api.NeutruxBlogsApi.security.UsersSecurityDetails;
import com.neutrux.api.NeutruxBlogsApi.service.UsersService;
import com.neutrux.api.NeutruxBlogsApi.shared.UserDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

@Service
public class UsersServiceImpl implements UsersService {

	private UsersRepository usersRepository;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = usersRepository.findByEmail(username);

		if (userEntity == null) {
			throw new UsernameNotFoundException(username + " not found!");
		}

		return new UsersSecurityDetails(userEntity);
	}

	@Override
	public UserDto getUserByUserId(String userId) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = null;
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		long id = decryptUserId(userId);
		userEntity = usersRepository.findById(id).get();

		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		String newUserId = encryptUserId(userEntity.getId());
		userDto.setUserId(newUserId);

		return userDto;
	}

	// Add some logic to avoid exposing database-userId
	// In Authentication API as well
	public String encryptUserId(long id) {
		return (id * 673926356) + "";
	}

	public long decryptUserId(String userId) throws Exception {
		long id = 0;
		try {
			id = Long.parseLong(userId);
		} catch (NumberFormatException e) {
			throw new Exception("User ID cannot be null!");
		}
		id = id / 673926356;
		return id;
	}

}