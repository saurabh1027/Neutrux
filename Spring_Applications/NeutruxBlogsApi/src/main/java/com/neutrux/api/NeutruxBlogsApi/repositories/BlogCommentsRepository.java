package com.neutrux.api.NeutruxBlogsApi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogCommentEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;

@Repository
public interface BlogCommentsRepository extends PagingAndSortingRepository<BlogCommentEntity, Long> {

	public Page<BlogCommentEntity> findAllByBlog( BlogEntity blog, Pageable commentPageable );
	
}