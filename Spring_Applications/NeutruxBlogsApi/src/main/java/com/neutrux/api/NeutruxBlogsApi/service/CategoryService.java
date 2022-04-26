package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.CategoryDto;

public interface CategoryService {

	public Set<CategoryDto> getCategories(int pageNumber, int pageLimit);
	
	public CategoryDto getCategoryById(String categoryId) throws Exception;
	
	public CategoryDto addCategory(CategoryDto categoryDto) throws Exception;
	
	public CategoryDto updateCategory(CategoryDto categoryDto) throws Exception;

	public void deleteCategory(String categoryId) throws Exception;
	
}