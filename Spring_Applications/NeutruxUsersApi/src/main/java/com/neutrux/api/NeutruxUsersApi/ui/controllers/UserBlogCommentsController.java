package com.neutrux.api.NeutruxUsersApi.ui.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.neutrux.api.NeutruxUsersApi.ui.models.request.BlogCommentRequestModel;

@RestController
@RequestMapping("users/{userId}/blogs/{blogId}/comments")
public class UserBlogCommentsController {

	private String BlogsApiUrl = null;
	private RestTemplate restTemplate;
	private Environment environment;
	
	@Autowired
	public UserBlogCommentsController(
		RestTemplate restTemplate,
		Environment environment
	) {
		this.environment = environment;
		this.restTemplate = restTemplate;
		this.BlogsApiUrl = this.environment.getProperty("com.neutrux.api.NeutruxBlogsApi.url");
	}
	
	@PreAuthorize("principal == #userId")
	@PostMapping
	public ResponseEntity<Object> addComment(
		@PathVariable("blogId") String blogId,
		@PathVariable("userId") String userId,
		@Valid @RequestBody BlogCommentRequestModel requestModel,
		@RequestHeader("Authorization") String access_token
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>(requestModel, headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId + "/comments/")
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Object.class, params);
	}
	
	@PreAuthorize("principal == #userId")
	@PutMapping("{commentId}")
	public ResponseEntity<Object> updateCommentByCommentId(
		@PathVariable("blogId") String blogId,
		@PathVariable("userId") String userId,
		@PathVariable("commentId") String commentId,
		@Valid @RequestBody BlogCommentRequestModel requestModel,
		@RequestHeader("Authorization") String access_token
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);
		
		HttpEntity<Object> entity = new HttpEntity<Object>(requestModel, headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId + "/comments/" + commentId)
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Object.class, params);
	}
	
	@PreAuthorize("principal == #userId")
	@DeleteMapping("{commentId}")
	public ResponseEntity<Object> deleteCommentByCommentId(
		@PathVariable("blogId") String blogId,
		@PathVariable("userId") String userId,
		@PathVariable("commentId") String commentId,
		@RequestHeader("Authorization") String access_token
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);
		
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);
		
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId + "/comments/" + commentId)
				.queryParam("X-User-ID", userId).build();
		
		return restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class, params);
	}
	
}
