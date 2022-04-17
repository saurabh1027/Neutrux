package com.neutrux.api.NeutruxBlogSearchApi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.CategoryEntity;

@Repository
public interface BlogSearchRepository extends PagingAndSortingRepository<BlogEntity, Long> {
	
	public Page<BlogEntity> findByTitleContains(String titleSubstr, Pageable blogPageable);
	
	public Page<BlogEntity> findAllBycategory(CategoryEntity category, Pageable blogPageable);
	
}