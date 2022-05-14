package com.neutrux.api.NeutruxBlogsApi.ui.controllers;

import java.util.Date;
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

import com.neutrux.api.NeutruxBlogsApi.service.BlogCommentsService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogCommentDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.request.BlogCommentRequestModel;
import com.neutrux.api.NeutruxBlogsApi.ui.models.response.SuccessMessageResponseModel;

@RestController
@RequestMapping("blogs/{blogId}/comments")
public class BlogCommentsController {
	
	private BlogCommentsService blogCommentsService;
	
	@Autowired
	public BlogCommentsController(
		BlogCommentsService blogCommentsService
	) {
		this.blogCommentsService = blogCommentsService;
	}
	
	
	@GetMapping
	public Set<BlogCommentDto> getCommentsOfBlog(
		@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
		@RequestParam(name = "pageLimit", defaultValue = "20") int pageLimit,
		@PathVariable("blogId") String blogIdStr
	) throws Exception {
		return this.blogCommentsService.getCommentsOfBlog( blogIdStr, pageNumber-1, pageLimit );
	}
	
	@GetMapping("{commentId}")
	public BlogCommentDto getCommentByCommentId(
		@PathVariable("commentId") String commentIdStr
	) throws Exception {
		return this.blogCommentsService.getCommentByCommentId( commentIdStr );
	}

	@PreAuthorize("principal == #userIdStr")
	@PostMapping
	public ResponseEntity<BlogCommentDto> addCommentToBlog(
		@PathVariable("blogId") String blogIdStr,
		@RequestParam("X-User-ID") String userIdStr,
		@Valid @RequestBody BlogCommentRequestModel requestModel
	) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		BlogCommentDto blogCommentDto = mapper.map(requestModel, BlogCommentDto.class);
		blogCommentDto.setBlogId(blogIdStr);
		blogCommentDto.setUserId(userIdStr);
		
		blogCommentDto = this.blogCommentsService.addCommentToBlog( blogCommentDto );
		
		return ResponseEntity.ok( blogCommentDto );
	}
	
	@PreAuthorize("principal == #userIdStr")
	@PutMapping("{commentId}")
	public ResponseEntity<BlogCommentDto> updateComment(
		@PathVariable("commentId") String commentIdStr,
		@RequestParam("X-User-ID") String userIdStr,
		@Valid @RequestBody BlogCommentRequestModel requestModel
	) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		BlogCommentDto blogCommentDto = mapper.map(requestModel, BlogCommentDto.class);
		blogCommentDto.setCommentId(commentIdStr);
		blogCommentDto = this.blogCommentsService.updateCommentToBlog( blogCommentDto );
		
		return ResponseEntity.ok( blogCommentDto );
	}
	
	@PreAuthorize("principal == #userIdStr")
	@DeleteMapping("{commentId}")
	public ResponseEntity<SuccessMessageResponseModel> deleteCommentbyCommentId(
		@PathVariable("commentId") String commentIdStr,
		@RequestParam("X-User-ID") String userIdStr
	) throws Exception {
		try {
			this.blogCommentsService.deleteCommentByCommentId( commentIdStr );
		} catch (Exception e) {
			e.printStackTrace();
		}

		SuccessMessageResponseModel successMessageResponseModel = new SuccessMessageResponseModel(new Date(),
				HttpStatus.OK.value(), HttpStatus.OK,
				"Comment with ID " + commentIdStr + " has been deleted!");

		return ResponseEntity.status(HttpStatus.OK).body(successMessageResponseModel);
	}
	
	
	
}