package com.neutrux.api.NeutruxBlogsApi.ui.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

import com.neutrux.api.NeutruxBlogsApi.service.BlogElementsService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogElementDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.request.BlogElementRequestModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.BlogElementResponseModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.SuccessMessageResponseModel;

@RequestMapping("blogs/{blogId}/elements")
@RestController
public class BlogElementsController {

	private BlogElementsService blogElementsService;

	@Autowired
	public BlogElementsController(BlogElementsService blogElementsService) {
		this.blogElementsService = blogElementsService;
	}
	
	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@DeleteMapping
	public ResponseEntity<SuccessMessageResponseModel> deleteBlogElements(@PathVariable("blogId") String blogId,
			@RequestParam("X-User-ID") String userId) throws Exception {
		blogElementsService.deleteElementsByBlogId(blogId);

		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK, "Elements of blog with ID " + blogId + " has been deleted!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@GetMapping
	public ResponseEntity<Set<BlogElementResponseModel>> getBlogElements(@PathVariable("blogId") String blogId,
			@RequestParam("X-User-ID") String userId) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogElementResponseModel> elements = new HashSet<BlogElementResponseModel>();
		BlogElementDto elementDto = null;
		BlogElementResponseModel responseModel = null;

		Set<BlogElementDto> blogElementDtoList = blogElementsService.getElementsByBlogId(blogId, userId);
		Iterator<BlogElementDto> iterator = blogElementDtoList.iterator();

		while (iterator.hasNext()) {
			elementDto = iterator.next();
			responseModel = mapper.map(elementDto, BlogElementResponseModel.class);
			elements.add(responseModel);
		}

		return ResponseEntity.ok(elements);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@GetMapping("{elementId}")
	public ResponseEntity<BlogElementResponseModel> getBlogElementById(@PathVariable("elementId") String elementId,
			@PathVariable("blogId") String blogId, @RequestParam("X-User-ID") String userId) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogElementResponseModel blogElementResponseModel = null;
		BlogElementDto blogElementDto = null;

		blogElementDto = blogElementsService.getElementById(elementId, blogId);
		blogElementResponseModel = modelMapper.map(blogElementDto, BlogElementResponseModel.class);

		return new ResponseEntity<BlogElementResponseModel>(blogElementResponseModel, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@PostMapping
	public ResponseEntity<BlogElementResponseModel> createBlogElement(
			@Valid @RequestBody BlogElementRequestModel requestModel, @PathVariable("blogId") String blogId,
			@RequestParam("X-User-ID") String userId) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogElementDto blogElementDto = null;
		BlogElementResponseModel responseModel = null;

		blogElementDto = modelMapper.map(requestModel, BlogElementDto.class);
		blogElementDto.setBlogId(blogId);
		blogElementDto.setUserId(userId);
		blogElementDto = blogElementsService.addElementToBlog(blogElementDto);

		responseModel = modelMapper.map(blogElementDto, BlogElementResponseModel.class);
		return ResponseEntity.ok(responseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@PutMapping("{elementId}")
	public ResponseEntity<BlogElementResponseModel> updateBlogElementById(@RequestParam("X-User-ID") String userId,
			@PathVariable("blogId") String blogId, @PathVariable("elementId") String elementId,
			@Valid @RequestBody BlogElementRequestModel requestModel) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BlogElementDto blogElementDto = mapper.map(requestModel, BlogElementDto.class);
		blogElementDto.setUserId(userId);
		blogElementDto.setBlogId(blogId);
		blogElementDto.setElementId(elementId);
		blogElementDto = blogElementsService.updateElementById(blogElementDto);

		BlogElementResponseModel responseModel = mapper.map(blogElementDto, BlogElementResponseModel.class);
		return ResponseEntity.ok(responseModel);
	}

	@PreAuthorize("hasRole('ROLE_EDITOR') and principal==#userId")
	@DeleteMapping("{elementId}")
	public ResponseEntity<SuccessMessageResponseModel> deleteBlogElementById(
			@PathVariable("elementId") String elementId, @RequestParam("X-User-ID") String userId) throws Exception {
		blogElementsService.deleteElementById(elementId);

		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK, "Element with ID " + elementId + " has been deleted!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}

}