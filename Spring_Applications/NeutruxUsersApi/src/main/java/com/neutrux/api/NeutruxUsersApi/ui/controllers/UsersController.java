package com.neutrux.api.NeutruxUsersApi.ui.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neutrux.api.NeutruxUsersApi.service.UsersService;
import com.neutrux.api.NeutruxUsersApi.shared.UserDto;
import com.neutrux.api.NeutruxUsersApi.ui.models.request.CreateUserRequestModel;
import com.neutrux.api.NeutruxUsersApi.ui.models.request.UpdateUserRequestModel;
import com.neutrux.api.NeutruxUsersApi.ui.models.response.SuccessMessageResponseModel;
import com.neutrux.api.NeutruxUsersApi.ui.models.response.UserResponseModel;

@RestController
@RequestMapping("/users")
public class UsersController {

	private Environment environment;
	private UsersService usersService;

	@Autowired
	public UsersController(Environment environment, UsersService usersService) {
		this.environment = environment;
		this.usersService = usersService;
	}

	@GetMapping("/status")
	public String checkStatus() {
		return "Working on port: " + environment.getProperty("local.server.port");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Set<UserResponseModel>> getUsers(
		@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
		@RequestParam(name = "pageLimit", defaultValue = "20") int pageLimit
	) {
		Set<UserResponseModel> users = new HashSet<UserResponseModel>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Set<UserDto> userDtoSet = usersService.getUsers(pageNumber-1, pageLimit);
		Iterator<UserDto> iterator = userDtoSet.iterator();

		while (iterator.hasNext()) {
			UserResponseModel userResponseModel = modelMapper.map(iterator.next(), UserResponseModel.class);
			users.add(userResponseModel);
		}

		return ResponseEntity.ok(users);
	}

	@PostMapping
	public ResponseEntity<UserResponseModel> createUser(
			@Valid @RequestBody CreateUserRequestModel createUserRequestModel) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		UserDto createdUser = null;
		UserDto userDto = null;
		UserResponseModel userResponseModel = null;

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		userDto = modelMapper.map(createUserRequestModel, UserDto.class);

		createdUser = usersService.createUser(userDto);

		userResponseModel = modelMapper.map(createdUser, UserResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(userResponseModel);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or principal == #userId")
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseModel> getUserByUserId(@PathVariable("userId") String userId) {
		ModelMapper modelMapper = new ModelMapper();
		UserResponseModel userResponseModel = null;
		UserDto userDto = null;

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		userDto = usersService.getUserByUserId(userId);

		userResponseModel = modelMapper.map(userDto, UserResponseModel.class);
		return new ResponseEntity<UserResponseModel>(userResponseModel, HttpStatus.OK);
	}

	@PreAuthorize("principal == #userId")
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponseModel> updateUserByUserId(@PathVariable("userId") String userId,
			@Valid @RequestBody UpdateUserRequestModel updateUserRequestModel) {

		UserDto newUserDetails = null;
		UserDto updatedUserDetails = null;
		ModelMapper modelMapper = new ModelMapper();
		UserResponseModel userResponseModel = null;

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		newUserDetails = modelMapper.map(updateUserRequestModel, UserDto.class);
		newUserDetails.setUserId(userId);

		updatedUserDetails = usersService.updateUserByUserId(newUserDetails);

		userResponseModel = modelMapper.map(updatedUserDetails, UserResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(userResponseModel);
	}

	@PreAuthorize("principal == #userId")
	@DeleteMapping("/{userId}")
	public ResponseEntity<SuccessMessageResponseModel> deleteUserByUserId(@PathVariable("userId") String userId) {
		usersService.deleteUserByUserId(userId);

		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK, "Account of User with ID " + userId + " has been deleted!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}

}
