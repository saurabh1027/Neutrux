package com.neutrux.api.NeutruxAuthenticationApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxAuthenticationApi.ui.models.UserEntity;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
}