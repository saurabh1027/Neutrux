package com.neutrux.api.NeutruxBlogsApi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

@Repository
public interface BlogsRepository extends PagingAndSortingRepository<BlogEntity, Long> {
	
	Page<BlogEntity> findAllByUser( UserEntity userEntity, Pageable blogPageable );
	
}