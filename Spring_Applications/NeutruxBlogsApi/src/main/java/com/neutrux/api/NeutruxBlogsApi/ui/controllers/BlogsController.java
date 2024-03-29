package com.neutrux.api.NeutruxBlogsApi.ui.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import com.neutrux.api.NeutruxBlogsApi.service.BlogsService;
import com.neutrux.api.NeutruxBlogsApi.service.CategoryService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.request.BlogRequestModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.BlogResponseModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.SuccessMessageResponseModel;

@RestController
@RequestMapping("blogs")
public class BlogsController {

	private BlogsService blogsService;
	private CategoryService categoryService;

	@Autowired
	public BlogsController(
		BlogsService blogsService,
		CategoryService categoryService) {
		this.blogsService = blogsService;
		this.categoryService = categoryService;
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal == #userId")
	@GetMapping
	public ResponseEntity<List<BlogResponseModel>> getUserBlogsByUserId(
		@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
		@RequestParam(name = "pageLimit", defaultValue = "20") int pageLimit,
		@RequestParam("X-User-ID") String userId,
		@RequestParam(name = "include-impressions", defaultValue = "false") boolean includeImpressions,
		@RequestParam(name = "include-comments", defaultValue = "false") boolean includeComments
	) throws Exception {
		List<BlogResponseModel> blogs = new ArrayList<BlogResponseModel>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		// Implement Blog Search Service here...
		List<BlogDto> blogsDto = blogsService.getBlogsByUserId(userId, includeImpressions, pageNumber - 1, pageLimit, includeComments);

		Iterator<BlogDto> iterator = blogsDto.iterator();
		while (iterator.hasNext()) {
			BlogResponseModel blogResponseModel = modelMapper.map(iterator.next(), BlogResponseModel.class);
			blogs.add(blogResponseModel);
		}
		
		return ResponseEntity.ok(blogs);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal == #userId")
	@GetMapping("{blogId}")
	public ResponseEntity<BlogResponseModel> getBlogByBlogId(@PathVariable("blogId") String blogId,
		@RequestParam("X-User-ID") String userId,
		@RequestParam(name = "include-impressions", defaultValue = "false") boolean includeImpressions,
		@RequestParam(name = "include-comments", defaultValue = "false") boolean includeComments
	) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogResponseModel blogResponseModel = null;
		BlogDto blogDto = null;

		blogDto = blogsService.getBlogByBlogId(blogId, userId, includeImpressions, includeComments);
		blogResponseModel = modelMapper.map(blogDto, BlogResponseModel.class);
		
		return new ResponseEntity<BlogResponseModel>(blogResponseModel, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal == #blogRequestModel.userId")
	@PostMapping
	public ResponseEntity<BlogResponseModel> createBlog(@RequestBody @Valid BlogRequestModel blogRequestModel)
			throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BlogDto blogDto = modelMapper.map(blogRequestModel, BlogDto.class);
		blogDto.setCategory( categoryService.getCategoryById( blogRequestModel.getCategoryId() ) );
		BlogDto createdBlog = blogsService.createBlog(blogDto);

		BlogResponseModel blogResponseModel = modelMapper.map(createdBlog, BlogResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(blogResponseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal == #blogRequestModel.userId")
	@PutMapping("{blogId}")
	public ResponseEntity<BlogResponseModel> updateBlogByBlogId(@PathVariable("blogId") String blogId,
			@RequestBody @Valid BlogRequestModel blogRequestModel) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BlogDto newBlogDetails = modelMapper.map(blogRequestModel, BlogDto.class);
		newBlogDetails.setBlogId(blogId);
		newBlogDetails.setCategory( categoryService.getCategoryById( blogRequestModel.getCategoryId() ) );
		
		BlogDto updatedBlog = blogsService.updateBlog(newBlogDetails);

		BlogResponseModel blogResponseModel = modelMapper.map(updatedBlog, BlogResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(blogResponseModel);
	}

	@PreAuthorize("principal==#userId and hasRole('ROLE_EDITOR')")
	@DeleteMapping("{blogId}")
	public ResponseEntity<SuccessMessageResponseModel> deleteBlogbyBlogId(@PathVariable("blogId") String blogId,
			@RequestParam("X-User-ID") String userId) throws Exception {
		try {
			blogsService.deleteBlogByBlogId(blogId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK,
				"Blog with ID " + blogId + " of User with ID " + userId + " has been deleted!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}

}
