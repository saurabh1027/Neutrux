package com.neutrux.api.NeutruxBlogSearchApi.ui.controllers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neutrux.api.NeutruxBlogSearchApi.service.BlogSearchService;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogDto;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.response.BlogResponseModel;

@RestController
@RequestMapping("blogs")
public class BlogSearchController {
	
	private BlogSearchService blogSearchService;
	
	@Autowired
	public BlogSearchController(
		BlogSearchService blogSearchService
	) {
		this.blogSearchService = blogSearchService;
	}
	
	
	@GetMapping
	public Set<BlogResponseModel> getBlogs(
		@RequestParam(name= "pageNumber", defaultValue = "1") int pageNumber,
		@RequestParam(name= "pageLimit", defaultValue = "20") int pageLimit
	){
		Set<BlogDto> blogDtos = blogSearchService.getBlogs(pageNumber-1, pageLimit);
		Set<BlogResponseModel> blogs = this.convertDtoToResponseModelList(blogDtos);
		return blogs;
	}
	
	@GetMapping("search/t/{titleSubstr}")
	public Set<BlogResponseModel> getBlogsByTitle(
		@PathVariable("titleSubstr") String titleSubstr,
		@RequestParam(name= "pageNumber", defaultValue = "1") int pageNumber,
		@RequestParam(name= "pageLimit", defaultValue = "20") int pageLimit
	) {
		Set<BlogDto> blogDtos = blogSearchService.getBlogsByTitleSubstring(titleSubstr, pageNumber-1, pageLimit);
		Set<BlogResponseModel> blogs = this.convertDtoToResponseModelList(blogDtos);
		return blogs;
	}
	
	@GetMapping("search/c/{category}")
	public Set<BlogResponseModel> getBlogsByCategory(
		@PathVariable("category") String category,
		@RequestParam(name= "pageNumber", defaultValue = "1") int pageNumber,
		@RequestParam(name= "pageLimit", defaultValue = "20") int pageLimit
	) {
		Set<BlogDto> blogDtos = this.blogSearchService.getBlogsByCategory(category, pageNumber-1, pageLimit);
		Set<BlogResponseModel> blogs = this.convertDtoToResponseModelList(blogDtos);
		return blogs;
	}

	
	
	
	public Set<BlogResponseModel> convertDtoToResponseModelList(Set<BlogDto> blogDtos) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Iterator<BlogDto> iterator = blogDtos.iterator();
		Set<BlogResponseModel> blogResponseModels = new HashSet<BlogResponseModel>();
		BlogResponseModel blogResponseModel = null;
		BlogDto blogDto = null;
		
		while(iterator.hasNext()) {
			blogDto = iterator.next();
			blogResponseModel = mapper.map(blogDto, BlogResponseModel.class);
			blogResponseModels.add(blogResponseModel);
		}
		
		return blogResponseModels;
	}
	
}