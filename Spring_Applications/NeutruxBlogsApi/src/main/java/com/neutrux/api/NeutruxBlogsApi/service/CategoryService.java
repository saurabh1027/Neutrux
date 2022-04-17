package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.CategoryDto;

public interface CategoryService {

	public Set<CategoryDto> getCategories(int pageNumber, int pageLimit);
	
}