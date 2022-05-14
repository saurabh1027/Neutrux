package com.neutrux.api.NeutruxBlogSearchApi.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("blogs/{blogId}/impressions")
public class BlogImpressionsController {

	private String BlogsApiUrl = null;
	private RestTemplate restTemplate;
	private Environment environment;

	@Autowired
	public BlogImpressionsController(
		RestTemplate restTemplate,
		Environment environment) {
		this.restTemplate = restTemplate;
		this.environment = environment;
		this.BlogsApiUrl = this.environment.getProperty("com.neutrux.api.NeutruxBlogsApi.url");
	}
	
	@GetMapping("count")
	public ResponseEntity<Object> getBlogImpressionsCount(@PathVariable("blogId") String blogId) {
		ResponseEntity<Object> responseEntity = restTemplate.exchange(this.BlogsApiUrl + blogId + "/impressions/count", HttpMethod.GET, null,
				Object.class);

		return responseEntity;
	}
	
}
