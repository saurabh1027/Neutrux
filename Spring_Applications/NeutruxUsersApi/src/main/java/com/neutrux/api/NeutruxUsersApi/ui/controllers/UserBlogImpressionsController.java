package com.neutrux.api.NeutruxUsersApi.ui.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.neutrux.api.NeutruxUsersApi.ui.models.request.BlogImpressionRequestModel;

@RestController
@RequestMapping("users/{userId}/blogs/{blogId}/impressions")
public class UserBlogImpressionsController {

	private String BlogsApiUrl = null;
	private RestTemplate restTemplate;
	private Environment environment;
	
	@Autowired
	public UserBlogImpressionsController(
		RestTemplate restTemplate,
		Environment environment
	) {
		this.environment = environment;
		this.restTemplate = restTemplate;
		this.BlogsApiUrl = this.environment.getProperty("com.neutrux.api.NeutruxBlogsApi.url");
	}
	
	@PreAuthorize("hasRole('ROLE_SUBSCRIBER') && principal==#blogImpressionRequestModel.userId")
	@PutMapping
	public ResponseEntity<Object> addOrUpdateImpressionToBlog(
		@PathVariable("blogId") String blogId,
		@PathVariable("userId") String userId,
		@RequestHeader("Authorization") String access_token,
		@RequestBody BlogImpressionRequestModel blogImpressionRequestModel
	) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>(blogImpressionRequestModel, headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId + "/impressions/")
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Object.class, params);
	}
	
	@PreAuthorize("hasRole('ROLE_SUBSCRIBER') && principal==#userId")
	@DeleteMapping
	public ResponseEntity<Object> removeImpressionFromBlog(
			@PathVariable("blogId") String blogId, @PathVariable("userId") String userId,
			@RequestHeader("Authorization") String access_token
	) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);
		
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);
		
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId + "/impressions/")
				.queryParam("X-User-ID", userId).build();
		
		return restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class, params);
	}
	
}
