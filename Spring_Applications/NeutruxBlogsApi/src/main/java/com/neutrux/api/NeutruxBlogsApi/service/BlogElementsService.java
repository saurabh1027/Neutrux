package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.BlogElementDto;

public interface BlogElementsService {

	public Set<BlogElementDto> getElementsByBlogId(String blogId, String userId);

	public BlogElementDto getElementById(String elementId, String blogId) throws Exception;
	
	public BlogElementDto addElementToBlog( BlogElementDto blogElementDto ) throws Exception;
	
	public BlogElementDto updateElementById( BlogElementDto blogElementDto ) throws Exception;
	
	public void deleteElementById( String elementId ) throws Exception;


}
