package com.neutrux.server.NeutruxFileServer.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.server.NeutruxFileServer.ui.models.UserEntity;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	
	List<UserEntity> findAllByFirstname(String firstname, Pageable pageable);

}