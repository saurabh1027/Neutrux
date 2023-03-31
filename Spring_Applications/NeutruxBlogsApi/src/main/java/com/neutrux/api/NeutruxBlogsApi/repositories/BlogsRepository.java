package com.neutrux.api.NeutruxBlogsApi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;

@Repository
public interface BlogsRepository extends PagingAndSortingRepository<BlogEntity, Long> {
	
//	This method is implemented with custom query in below method
//	Page<BlogEntity> findAllByUser( UserEntity userEntity, Pageable blogPageable );
	
	@Query(
			value = "SELECT * FROM blogs WHERE user_id=?1 ORDER BY creation_date DESC",
			nativeQuery = true)
	Page<BlogEntity> findAllByUser( long user_id, Pageable blogPageable );
	
}