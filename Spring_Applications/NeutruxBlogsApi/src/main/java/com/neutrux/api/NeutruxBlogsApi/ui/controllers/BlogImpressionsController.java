package com.neutrux.api.NeutruxBlogsApi.ui.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neutrux.api.NeutruxBlogsApi.service.BlogImpressionsService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogImpressionDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.request.BlogImpressionRequestModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.SuccessMessageResponseModel;

@RestController
@RequestMapping("blogs/{blogId}/impressions")
public class BlogImpressionsController {

	private BlogImpressionsService blogImpressionsService;

	@Autowired
	public BlogImpressionsController(BlogImpressionsService blogImpressionsService) {
		this.blogImpressionsService = blogImpressionsService;
	}

	@GetMapping("count")
	public long getBlogImpressionsCount(@PathVariable("blogId") String blogId) throws Exception {
		return this.blogImpressionsService.getBlogImpressionsCount(blogId);
	}
	
	@GetMapping
	public Set<BlogImpressionDto> getBlogImpressions(@PathVariable("blogId") String blogId) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Set<BlogImpressionDto> impressions = new HashSet<BlogImpressionDto>();
		impressions = this.blogImpressionsService.getImpressionsByBlogId(blogId);
		return impressions;
	}

	@PreAuthorize("hasRole('ROLE_SUBSCRIBER') && principal==#blogImpressionRequestModel.userId")
	@PutMapping
	public ResponseEntity<BlogImpressionDto> addOrUpdateImpressionToBlog(@PathVariable("blogId") String blogId,
			@RequestBody BlogImpressionRequestModel blogImpressionRequestModel) throws Exception {

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogImpressionDto blogImpressionDto = mapper.map(blogImpressionRequestModel, BlogImpressionDto.class);
		blogImpressionDto.setBlogId(blogId);

		blogImpressionDto = this.blogImpressionsService.addOrUpdateImpressionToBlog(blogImpressionDto);

		return ResponseEntity.ok(blogImpressionDto);
	}

	@PreAuthorize("hasRole('ROLE_SUBSCRIBER') && principal==#userId")
	@DeleteMapping
	public ResponseEntity<SuccessMessageResponseModel> removeImpressionFromBlog(@PathVariable("blogId") String blogId,
			@RequestParam("X-User-ID") String userId) throws Exception {
		this.blogImpressionsService.removeImpressionFromBlog(blogId, userId);
		
		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK, "Impression is deleted successfully!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}

}