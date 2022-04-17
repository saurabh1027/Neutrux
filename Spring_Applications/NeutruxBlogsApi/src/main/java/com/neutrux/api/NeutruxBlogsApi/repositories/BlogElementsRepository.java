package com.neutrux.api.NeutruxBlogsApi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogElementEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;

@Repository
public interface BlogElementsRepository extends PagingAndSortingRepository<BlogElementEntity, Long> {
	
	public BlogElementEntity findByPositionAndBlog(long position, BlogEntity blog);
	
}