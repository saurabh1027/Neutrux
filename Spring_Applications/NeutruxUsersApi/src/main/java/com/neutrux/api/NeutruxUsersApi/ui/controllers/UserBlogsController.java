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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.neutrux.api.NeutruxUsersApi.ui.models.request.BlogRequestModel;

@RestController
@RequestMapping("users/{userId}/blogs")
public class UserBlogsController {

	private String BlogsApiUrl = null;
	private RestTemplate restTemplate;
	private Environment environment;

	@Autowired
	public UserBlogsController(
		RestTemplate restTemplate,
		Environment environment) {
		this.restTemplate = restTemplate;
		this.environment = environment;
		this.BlogsApiUrl = this.environment.getProperty("com.neutrux.api.NeutruxBlogsApi.url");
	}

	@GetMapping
	public ResponseEntity<Object> getUserBlogs(@RequestHeader("Authorization") String access_token,
			@PathVariable("userId") String userId,
			@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(name = "pageLimit", defaultValue = "20") int pageLimit) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);
		params.put("pageNumber", pageNumber + "");
		params.put("pageLimit", pageLimit + "");

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl).queryParam("X-User-ID", userId)
				.queryParam("pageNumber", pageNumber).queryParam("pageLimit", pageLimit).build();

		ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Object.class, params);
		
		return responseEntity;
	}

	@GetMapping("{blogId}")
	public ResponseEntity<Object> getUserBlogByBlogId(@RequestHeader("Authorization") String access_token,
			@PathVariable("userId") String userId, @PathVariable("blogId") String blogId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId)
				.queryParam("X-User-ID", userId).build();

		ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Object.class, params);

		return responseEntity;
	}

	@PostMapping
	public ResponseEntity<Object> addBlog(@RequestHeader("Authorization") String access_token,
			@RequestBody @Valid BlogRequestModel blogRequestModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<BlogRequestModel> entity = new HttpEntity<BlogRequestModel>(blogRequestModel, headers);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl).build();

		ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
				Object.class);

		return responseEntity;
	}

	@PutMapping("{blogId}")
	public ResponseEntity<Object> updateBlogById(@RequestHeader("Authorization") String access_token,
			@PathVariable("blogId") String blogId, @RequestBody @Valid BlogRequestModel blogRequestModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<BlogRequestModel> entity = new HttpEntity<BlogRequestModel>(blogRequestModel, headers);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId).build();

		ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity,
				Object.class);

		return responseEntity;
	}

	@DeleteMapping("{blogId}")
	public ResponseEntity<Object> deleteBlogById(@RequestHeader("Authorization") String access_token,
			@PathVariable("blogId") String blogId, @PathVariable("userId") String userId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", access_token);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Map<String, String> params = new HashMap<String, String>();
		params.put("X-User-ID", userId);

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(this.BlogsApiUrl + blogId)
				.queryParam("X-User-ID", userId).build();

		ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity,
				Object.class, params);

		return responseEntity;
	}

}