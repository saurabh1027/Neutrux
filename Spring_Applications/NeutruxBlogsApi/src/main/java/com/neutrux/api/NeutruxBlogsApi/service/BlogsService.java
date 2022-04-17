package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.BlogDto;

public interface BlogsService {
	
	Set<BlogDto> getBlogsByUserId( String userId, int pageNumber, int pageLimit ) throws Exception;

	BlogDto createBlog( BlogDto blogDto ) throws Exception ;
	
	BlogDto getBlogByBlogId( String blogId, String userId ) throws Exception;
	
	BlogDto updateBlog( BlogDto blogDto ) throws Exception;
	
	void deleteBlogByBlogId( String blogId ) throws Exception;
	
	
	
	String encryptId(long id);

	long decryptId(String blogId) throws Exception;

}
