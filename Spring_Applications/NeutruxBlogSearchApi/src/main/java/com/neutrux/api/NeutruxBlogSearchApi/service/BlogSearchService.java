package com.neutrux.api.NeutruxBlogSearchApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogDto;

public interface BlogSearchService {

	public Set<BlogDto> getBlogs(int pageNumber, int pageLimit);
	
	public Set<BlogDto> getBlogsByCategory(String category, int pageNumber, int pageLimit);
	
	public Set<BlogDto> getBlogsByTitleSubstring(String titleSubstr, int pageNumber, int pageLimit);
	//asc sort by date
	public Set<BlogDto> getLatestBlogs(int pageNumber, int pageLimit);

}
