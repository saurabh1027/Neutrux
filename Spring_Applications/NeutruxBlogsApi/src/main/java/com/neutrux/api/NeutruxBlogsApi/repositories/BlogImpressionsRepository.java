package com.neutrux.api.NeutruxBlogsApi.repositories;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogImpressionEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

public interface BlogImpressionsRepository extends PagingAndSortingRepository<BlogImpressionEntity, Long> {

	public long countByBlog( BlogEntity blog );
	
	public Set<BlogImpressionEntity> findByBlog( BlogEntity blog );
	
	public BlogImpressionEntity findByBlogAndUser( BlogEntity blog, UserEntity user );
	
	public BlogImpressionEntity findByBlogAndUserAndType( BlogEntity blog, UserEntity user, String type );
	
}