package com.neutrux.api.NeutruxBlogSearchApi.service.implementation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxBlogSearchApi.repositories.BlogSearchRepository;
import com.neutrux.api.NeutruxBlogSearchApi.repositories.CategoryRepository;
import com.neutrux.api.NeutruxBlogSearchApi.service.BlogSearchService;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogCommentDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogElementDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogImpressionDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.CategoryDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.UserDto;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogCommentEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogElementEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogImpressionEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.CategoryEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.UserEntity;

@Service
public class BlogSearchServiceImpl implements BlogSearchService {

	private BlogSearchRepository blogSearchRepository;
	private CategoryRepository categoryRepository;

	@Autowired
	public BlogSearchServiceImpl(BlogSearchRepository blogSearchRepository, CategoryRepository categoryRepository) {
		this.blogSearchRepository = blogSearchRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Set<BlogDto> getBlogs(int pageNumber, int pageLimit, boolean includeImpressions, boolean includeComments) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogDto> blogs = new HashSet<BlogDto>();
		BlogEntity blogEntity = null;
		BlogDto blogDto = null;

		Pageable blogsPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogEntity> blogsPage = this.blogSearchRepository.findAll(blogsPageable);
		Iterator<BlogEntity> blogsIterator = blogsPage.iterator();

		while (blogsIterator.hasNext()) {
			blogEntity = blogsIterator.next();
			String blogId = encryptId(blogEntity.getId());
			blogDto = this.getBlogDetails(blogEntity, blogId, includeImpressions, includeComments);
			blogs.add(blogDto);
		}

		return blogs;
	}

	@Override
	public Set<BlogDto> getBlogsByCategory(String categoryId, int pageNumber, int pageLimit,
			boolean includeComments, boolean includeImpressions) throws Exception {
		CategoryEntity categoryEntity = null;
		BlogEntity blogEntity = null;
		BlogDto blogDto = null;
		Set<BlogDto> blogDtos = new HashSet<BlogDto>();
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		long id = this.decryptId(categoryId);
		
		try {
			categoryEntity = categoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Category with id-"+categoryId+" not found!");
		}
		Pageable blogPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogEntity> blogsPage = blogSearchRepository.findAllBycategory(categoryEntity, blogPageable);
		Iterator<BlogEntity> blogsIterator = blogsPage.iterator();
		
		while (blogsIterator.hasNext()) {
			blogEntity = blogsIterator.next();
			String blogId = encryptId(blogEntity.getId());
			blogDto = this.getBlogDetails(blogEntity, blogId, includeImpressions, includeComments);
			blogDtos.add(blogDto);
		}
		
		return blogDtos;
	}

	@Override
	public Set<BlogDto> getBlogsByTitleSubstring(String titleSubstr, int pageNumber, int pageLimit,
			boolean includeImpressions, boolean includeComments) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogDto> blogs = new HashSet<BlogDto>();
		BlogEntity blogEntity = null;
		BlogDto blogDto = null;

		Pageable blogPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogEntity> blogsPage = blogSearchRepository.findByTitleContains(titleSubstr, blogPageable);
		Iterator<BlogEntity> blogsIterator = blogsPage.iterator();

		while (blogsIterator.hasNext()) {
			blogEntity = blogsIterator.next();
			String blogId = encryptId(blogEntity.getId());
			blogDto = this.getBlogDetails(blogEntity, blogId, includeImpressions, includeComments);
			blogs.add(blogDto);
		}

		return blogs;
	}

	@Override
	public Set<BlogDto> getLatestBlogs(int pageNumber, int pageLimit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BlogDto> getTrendingBlogs(int pageNumber, int pageLimit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlogDto getBlogById(String blogIdStr) throws Exception {
		BlogDto blogDto = null;
		BlogEntity blogEntity = null;
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		long blogId = this.decryptId(blogIdStr);
		try {
			blogEntity = this.blogSearchRepository.findById(blogId).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID:" + blogIdStr + " not found!");
		}

		blogDto = this.getBlogDetails(blogEntity, blogIdStr, true, true);
		return blogDto;
	}

	public BlogDto getBlogDetails(BlogEntity blogEntity, String blogId, boolean includeImpressions,
			boolean includeComments) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogImpressionDto> impressionDtoList = new HashSet<BlogImpressionDto>();
		Set<BlogCommentDto> commentDtos = new HashSet<BlogCommentDto>();
		BlogElementEntity element = null;
		BlogElementDto elementDto = null;
		CategoryEntity categoryEntity = null;
		CategoryDto categoryDto = null;
		UserEntity userEntity = null;
		UserDto userDto = null;

		userEntity = blogEntity.getUser();
		userDto = modelMapper.map(userEntity, UserDto.class);
		userDto.setUserId(this.encryptId(userEntity.getId()));

		categoryEntity = blogEntity.getCategory();
		categoryDto = modelMapper.map(categoryEntity, CategoryDto.class);
		categoryDto.setCategoryId(this.encryptId(categoryEntity.getId()));

		Set<BlogElementEntity> elementList = blogEntity.getElements();
		Iterator<BlogElementEntity> elementsIterator = elementList.iterator();
		Set<BlogElementDto> elementDtoList = new HashSet<BlogElementDto>();

		while (elementsIterator.hasNext()) {
			element = elementsIterator.next();
			elementDto = modelMapper.map(element, BlogElementDto.class);
			elementDto.setElementId(this.encryptId(element.getId()));
			elementDto.setBlogId(blogId);
			elementDtoList.add(elementDto);
		}

		if (includeComments) {
			BlogCommentEntity blogCommentEntity = null;
			BlogCommentDto blogCommentDto = null;
			Set<BlogCommentEntity> commentEntities = blogEntity.getComments();
			Iterator<BlogCommentEntity> commentsIterator = commentEntities.iterator();

			while (commentsIterator.hasNext()) {
				blogCommentEntity = commentsIterator.next();
				blogCommentDto = modelMapper.map(blogCommentEntity, BlogCommentDto.class);
				UserDto commentUserDto = blogCommentDto.getUser();
				blogCommentDto.setCommentId(this.encryptId(blogCommentEntity.getId()));
				blogCommentDto.setBlogId(this.encryptId(blogCommentEntity.getBlog().getId()));
				commentUserDto.setUserId(this.encryptId(blogCommentEntity.getUser().getId()));
				commentDtos.add(blogCommentDto);
			}
		}

		if (includeImpressions) {
			Set<BlogImpressionEntity> impressionList = blogEntity.getImpressions();
			Iterator<BlogImpressionEntity> impressionsIterator = impressionList.iterator();
			impressionDtoList = new HashSet<BlogImpressionDto>();

			while (impressionsIterator.hasNext()) {
				BlogImpressionEntity impression = impressionsIterator.next();
				BlogImpressionDto impressionDto = modelMapper.map(impression, BlogImpressionDto.class);
				impressionDto.setImpressionId(this.encryptId(impression.getId()));
				impressionDto.setBlogId(blogId);
				impressionDto.setUserId(this.encryptId(impression.getUser().getId()));
				impressionDtoList.add(impressionDto);
			}
		}

		BlogDto blogDto = modelMapper.map(blogEntity, BlogDto.class);
		blogDto.setCategory(categoryDto);
		blogDto.setUser(userDto);
		blogDto.setBlogId(blogId);
		blogDto.setElements(elementDtoList);
		blogDto.setImpressions(impressionDtoList);
		blogDto.setComments(commentDtos);
		blogDto.setImpressionsCount(blogEntity.getImpressions().size());

		return blogDto;
	}

	public String encryptId(long id) {
		return (id * 673926356) + "";
	}

	public long decryptId(String blogId) throws Exception {
		long id = 0;
		try {
			id = Long.parseLong(blogId);
		} catch (NumberFormatException e) {
			throw new Exception("Blog ID cannot be null!");
		}
		id = id / 673926356;
		return id;
	}

}