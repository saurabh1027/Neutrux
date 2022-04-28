package com.neutrux.api.NeutruxBlogsApi.service;

import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.BlogCommentDto;

public interface BlogCommentsService {
	
	public BlogCommentDto getCommentByCommentId(String commentIdStr) throws Exception;
	
	public Set<BlogCommentDto> getCommentsOfBlog(String blogIdStr, int pageNumber, int pageLimit) throws Exception;

	public BlogCommentDto addCommentToBlog(BlogCommentDto blogCommentDto) throws Exception;

	public BlogCommentDto updateCommentToBlog(BlogCommentDto blogCommentDto) throws Exception;

	public void deleteCommentByCommentId(String commentIdStr) throws Exception;

}
