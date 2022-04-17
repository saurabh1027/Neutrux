package com.neutrux.api.NeutruxUsersApi.service.implementation;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import com.neutrux.api.NeutruxUsersApi.repositories.UsersRepository;
import com.neutrux.api.NeutruxUsersApi.security.UsersSecurityDetails;
import com.neutrux.api.NeutruxUsersApi.service.UsersService;
import com.neutrux.api.NeutruxUsersApi.shared.UserDto;
import com.neutrux.api.NeutruxUsersApi.ui.models.UserEntity;

@Service
public class UsersServiceImpl implements UsersService {

	private UsersRepository usersRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
	public Set<UserDto> getUsers(int pageNumber, int pageLimit) {
		UserDto userDto = null;
		Set<UserDto> users = new HashSet<UserDto>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Pageable userPageables = PageRequest.of(pageNumber, pageLimit);
		Page<UserEntity> userPages = usersRepository.findAll(userPageables);
		
		Iterator<UserEntity> iterator = userPages.iterator();

		while (iterator.hasNext()) {
			UserEntity userEntity = iterator.next();
			userDto = modelMapper.map(userEntity, UserDto.class);
			userDto.setUserId(encryptUserId(userEntity.getId()));
			users.add(userDto);
		}

		return users;
	}

	@Override
	public UserDto createUser(UserDto userDetails) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		// Add algorithm to store encrypted password!
		String encryptedPassword = encryptPassword(userDetails.getPassword());
		userDetails.setEncryptedPassword(encryptedPassword);

		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		userEntity.setRoles("ROLE_SUBSCRIBER");

		try {
			userEntity = usersRepository.save(userEntity);
		} catch (DataIntegrityViolationException e) {
			// Exception Handling for Duplicate Entry of Email
			if (e.getRootCause() != null
					&& e.getRootCause().getClass().equals(SQLIntegrityConstraintViolationException.class)) {
				SQLIntegrityConstraintViolationException ex = (SQLIntegrityConstraintViolationException) e
						.getRootCause();
				if (ex.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
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
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = null;

		long id = decryptUserId(userId);
		userEntity = usersRepository.findById(id).get();

		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		String newUserId = encryptUserId(userEntity.getId());
		userDto.setUserId(newUserId);

		return userDto;
	}

	@Override
	public UserDto updateUserByUserId(UserDto newUserDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		long id = this.decryptUserId(newUserDetails.getUserId());
		UserEntity oldUserEntity = usersRepository.findById(id).get();

		oldUserEntity.setFirstname(newUserDetails.getFirstname());
		oldUserEntity.setLastname(newUserDetails.getLastname());

		UserEntity newUserEntity = oldUserEntity;
		UserEntity updatedUserEntity = usersRepository.save(newUserEntity);

		UserDto updatedUserDetails = modelMapper.map(updatedUserEntity, UserDto.class);

		String newUserId = encryptUserId(updatedUserEntity.getId());
		updatedUserDetails.setUserId(newUserId);

		return updatedUserDetails;
	}

	@Override
	public void deleteUserByUserId(String userId) {
		long id = this.decryptUserId(userId);

		UserEntity userEntity = usersRepository.findById(id).get();
		usersRepository.delete(userEntity);
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
		id = id / 673926356;
		return id;
	}

}