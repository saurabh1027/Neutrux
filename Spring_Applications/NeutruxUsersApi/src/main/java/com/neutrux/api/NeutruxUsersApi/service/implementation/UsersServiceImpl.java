package com.neutrux.api.NeutruxUsersApi.service.implementation;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import com.neutrux.api.NeutruxUsersApi.data.UserEntity;
import com.neutrux.api.NeutruxUsersApi.data.UsersRepository;
import com.neutrux.api.NeutruxUsersApi.service.UsersService;
import com.neutrux.api.NeutruxUsersApi.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

	private UsersRepository usersRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		// Add algorithm to store encrypted password!
		String encryptedPassword = encryptPassword(userDto.getPassword());
		userDto.setEncryptedPassword(encryptedPassword);

		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		
		userEntity.setRole("SUBSCRIBER");

		try {
			usersRepository.save(userEntity);
		} catch (DataIntegrityViolationException e) {
			//Exception Handling for Duplicate Entry of Email
			if (e.getRootCause() != null
					&& e.getRootCause().getClass().equals(SQLIntegrityConstraintViolationException.class)) {
				SQLIntegrityConstraintViolationException ex = (SQLIntegrityConstraintViolationException) e
						.getRootCause();
				if( ex.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY ) {
					throw new Exception("Email already exists!");
				}
			}
		}

		UserDto createdUser = modelMapper.map(userEntity, UserDto.class);

		String userId = encryptUserId(userEntity.getId());
		createdUser.setUserId(userId);

		return createdUser;
	}
	
	@Override
	public UserDto getUserByUserId(String userId) {
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = null;
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		long id = decryptUserId(userId);
		userEntity = usersRepository.findById(id).get();
		
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		userDto.setUserId(userId);
		
		return userDto;
	}

	// Add some logic to avoid exposing original user passwords
	private String encryptPassword(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

	// Add some logic to avoid exposing database-userId
	// In Authentication API as well
	private String encryptUserId(long id) {
		return (id * 673926356) + "";
	}
	
	private long decryptUserId(String userId) {
		long id = 0;
		id = Long.parseLong(userId);
		id = id/673926356;
		return id;
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