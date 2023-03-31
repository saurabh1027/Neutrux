package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.List;
import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.BlogDto;

public interface BlogsService {

	List<BlogDto> getBlogsByUserId(String userId, boolean includeImpressions, int pageNumber, int pageLimit,
			boolean includeComments) throws Exception;

	BlogDto createBlog(BlogDto blogDto) throws Exception;

	BlogDto getBlogByBlogId(String blogId, String userId, boolean includeImpressions, boolean includeComments)
			throws Exception;

	BlogDto updateBlog(BlogDto blogDto) throws Exception;

	void deleteBlogByBlogId(String blogId) throws Exception;

	String encryptId(long id);

	long decryptId(String blogId) throws Exception;

}
