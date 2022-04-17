package com.neutrux.api.NeutruxBlogSearchApi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogSearchApi.ui.models.CategoryEntity;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {
	
}