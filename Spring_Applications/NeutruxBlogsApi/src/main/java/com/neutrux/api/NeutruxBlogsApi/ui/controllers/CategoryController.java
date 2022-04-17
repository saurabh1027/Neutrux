package com.neutrux.api.NeutruxBlogsApi.ui.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neutrux.api.NeutruxBlogsApi.service.CategoryService;
import com.neutrux.api.NeutruxBlogsApi.shared.CategoryDto;

@RestController
@RequestMapping("categories")
public class CategoryController {

	private CategoryService categoryService;
	
	@Autowired
	public CategoryController(
		CategoryService categoryService
	) {
		this.categoryService = categoryService;
	}
	
	
	@GetMapping
	public Set<CategoryDto> getCategories(
		@RequestParam("pageNumber") int pageNumber,
		@RequestParam("pageLimit") int pageLimit
	){
		Set<CategoryDto> categories = new HashSet<CategoryDto>();
		
		categories = this.categoryService.getCategories(pageNumber, pageLimit);
		
		return categories;
	}
	
}