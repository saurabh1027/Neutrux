package com.neutrux.api.NeutruxBlogsApi.ui.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neutrux.api.NeutruxBlogsApi.service.CategoryService;
import com.neutrux.api.NeutruxBlogsApi.shared.CategoryDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.request.CategoryRequestModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.CategoryResponseModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.SuccessMessageResponseModel;

@RestController
@RequestMapping("categories")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public Set<CategoryDto> getCategories(@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(name = "pageLimit", defaultValue = "20") int pageLimit) {
		Set<CategoryDto> categories = new HashSet<CategoryDto>();

		categories = this.categoryService.getCategories(pageNumber - 1, pageLimit);
		
		return categories;
	}
	
	@GetMapping("{categoryId}")
	public ResponseEntity<CategoryResponseModel> getCategoryById(
		@PathVariable("categoryId") String categoryId
	) throws Exception {
		CategoryResponseModel categoryResponseModel = null;
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
		
		categoryResponseModel = mapper.map(categoryDto, CategoryResponseModel.class);
		
		return ResponseEntity.ok(categoryResponseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@PostMapping
	public ResponseEntity<CategoryResponseModel> addCategory(
			@Valid @RequestBody CategoryRequestModel categoryRequestModel, @RequestParam("X-User-ID") String userId)
			throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		CategoryDto categoryDto = mapper.map(categoryRequestModel, CategoryDto.class);
		categoryDto = this.categoryService.addCategory(categoryDto);

		CategoryResponseModel responseModel = mapper.map(categoryDto, CategoryResponseModel.class);

		return ResponseEntity.ok(responseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@PutMapping("{categoryId}")
	public ResponseEntity<CategoryResponseModel> updateCategory(
			@Valid @RequestBody CategoryRequestModel categoryRequestModel, @RequestParam("X-User-ID") String userId,
			@PathVariable("categoryId") String categoryId) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		CategoryDto categoryDto = mapper.map(categoryRequestModel, CategoryDto.class);
		categoryDto.setCategoryId(categoryId);
		categoryDto = this.categoryService.updateCategory(categoryDto);

		CategoryResponseModel responseModel = mapper.map(categoryDto, CategoryResponseModel.class);

		return ResponseEntity.ok(responseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@DeleteMapping("{categoryId}")
	public ResponseEntity<SuccessMessageResponseModel> deleteCategoryById(@PathVariable("categoryId") String categoryId,
			@RequestParam("X-User-ID") String userId) throws Exception {
		this.categoryService.deleteCategory(categoryId);
		
		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK, "Category with ID " + categoryId + " has been deleted!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}

}