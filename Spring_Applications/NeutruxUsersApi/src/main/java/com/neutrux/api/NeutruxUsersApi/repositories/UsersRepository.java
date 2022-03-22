package com.neutrux.api.NeutruxUsersApi.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.neutrux.api.NeutruxUsersApi.ui.models.UserEntity;

public interface UsersRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	
	List<UserEntity> findAllByFirstname(String firstname, Pageable pageable);

}