package com.neutrux.api.NeutruxBlogsApi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogsApi.ui.models.CategoryEntity;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {
	
}