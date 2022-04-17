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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

import com.neutrux.api.NeutruxUsersApi.ui.models.request.BlogElementRequestModel;

@RestController
@RequestMapping("users/{userId}/blogs/{blogId}/elements")
public class UserBlogElementsController {

	private String BlogElementsApiUrl = null;
	private RestTemplate restTemplate;
	private Environment environment;

	@Autowired
	public UserBlogElementsController(
		RestTemplate restTemplate,
		Environment environment) {
		this.restTemplate = restTemplate;
		this.environment = environment;
		this.BlogElementsApiUrl = this.environment.getProperty("com.neutrux.api.NeutruxBlogsApi.url");
	}

	@GetMapping
	public ResponseEntity<Object> getUserBlogElements(@RequestHeader("Authorization") String access_token,
			@PathVariable("userId") String userId, @PathVariable("blogId") String blogId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>(headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogElementsApiUrl + blogId + "/elements/")
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Object.class, params);
	}

	@GetMapping("{elementId}")
	public ResponseEntity<Object> getUserBlogElementByElementId(@RequestHeader("Authorization") String access_token,
			@PathVariable("userId") String userId, @PathVariable("blogId") String blogId,
			@PathVariable("elementId") String elementId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>(headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder
				.fromHttpUrl(this.BlogElementsApiUrl + blogId + "/elements/" + elementId)
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Object.class, params);
	}

	@PostMapping
	public ResponseEntity<Object> addUserBlogElement(@RequestHeader("Authorization") String access_token,
			@PathVariable("userId") String userId, @PathVariable("blogId") String blogId,
			@RequestBody() BlogElementRequestModel blogElementRequestModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>(blogElementRequestModel, headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogElementsApiUrl + blogId + "/elements/")
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Object.class, params);
	}

	@PutMapping("{elementId}")
	public ResponseEntity<Object> updateUserBlogElementByElementId(@RequestHeader("Authorization") String access_token,
			@PathVariable("userId") String userId, @PathVariable("blogId") String blogId,
			@PathVariable("elementId") String elementId, @RequestBody BlogElementRequestModel blogElementRequestModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>(blogElementRequestModel, headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder
				.fromHttpUrl(this.BlogElementsApiUrl + blogId + "/elements/" + elementId)
				.queryParam("X-User-ID", userId).build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Object.class, params);
	}

	@DeleteMapping("{elementId}")
	public ResponseEntity<Object> deleteUserBlogElementByElementId(
		@RequestHeader("Authorization") String access_token,
		@PathVariable("userId") String userId, 
		@PathVariable("blogId") String blogId,
		@PathVariable("elementId") String elementId
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<Object> entity = new HttpEntity<Object>( headers );

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder
				.fromHttpUrl(this.BlogElementsApiUrl + blogId + "/elements/" + elementId)
				.queryParam("X-User-ID", userId)
				.build();

		return restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class, params);
	}

}