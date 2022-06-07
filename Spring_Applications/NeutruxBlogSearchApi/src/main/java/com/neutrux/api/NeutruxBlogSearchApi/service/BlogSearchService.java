package com.neutrux.api.NeutruxBlogSearchApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogDto;

public interface BlogSearchService {

	public Set<BlogDto> getBlogs(int pageNumber, int pageLimit, boolean includeImpressions, boolean includeComments);

	public Set<BlogDto> getBlogsByCategory(String category, int pageNumber, int pageLimit,
			boolean includeComments, boolean includeImpressions) throws Exception;

	// asc sort by date
	public Set<BlogDto> getLatestBlogs(int pageNumber, int pageLimit);

	public Set<BlogDto> getTrendingBlogs(int pageNumber, int pageLimit);

	public Set<BlogDto> getBlogsByTitleSubstring(String titleSubstr, int pageNumber, int pageLimit,
			boolean includeImpressions, boolean includeComments);
	
	public BlogDto getBlogById( String blogId ) throws Exception;

}
