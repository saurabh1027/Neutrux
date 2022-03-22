package com.neutrux.api.NeutruxAuthenticationApi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.neutrux.api.NeutruxAuthenticationApi.ui.models.UserEntity;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
}