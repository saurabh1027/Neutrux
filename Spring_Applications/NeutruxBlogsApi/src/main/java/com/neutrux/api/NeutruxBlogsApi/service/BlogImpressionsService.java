package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.BlogImpressionDto;

public interface BlogImpressionsService {
	
	public long getBlogImpressionsCount(String blogId) throws Exception;
	
	public Set<BlogImpressionDto> getImpressionsByBlogId(String blogId) throws Exception;

	public BlogImpressionDto addOrUpdateImpressionToBlog(BlogImpressionDto blogImpressionDto) throws Exception;
	
	public void removeImpressionFromBlog(String blogId, String userId) throws Exception;
}
