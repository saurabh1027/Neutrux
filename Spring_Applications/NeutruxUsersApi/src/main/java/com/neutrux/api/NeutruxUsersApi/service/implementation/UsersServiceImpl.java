package com.neutrux.api.NeutruxUsersApi.service.implementation;

import com.neutrux.api.NeutruxUsersApi.data.UserEntity;
import com.neutrux.api.NeutruxUsersApi.data.UsersRepository;
import com.neutrux.api.NeutruxUsersApi.service.UsersService;
import com.neutrux.api.NeutruxUsersApi.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersServiceImpl(
            UsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ){
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //Add algorithm to store encrypted password!
        String encryptedPassword = encryptPassword(userDto.getPassword());
        userDto.setEncryptedPassword(encryptedPassword);

        UserEntity userEntity = modelMapper.map(userDto , UserEntity.class);
        usersRepository.save(userEntity);

        UserDto createdUser = modelMapper.map(userEntity, UserDto.class);

        String userId = encryptUserId(userEntity.getId());
        createdUser.setUserId(userId);

        return createdUser;
    }

    //Add some logic to avoid exposing original user passwords
    private String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    //Add some logic to avoid exposing database-userId
    private String encryptUserId(long id) {
        return (id * 673926356)+"";
    }
}