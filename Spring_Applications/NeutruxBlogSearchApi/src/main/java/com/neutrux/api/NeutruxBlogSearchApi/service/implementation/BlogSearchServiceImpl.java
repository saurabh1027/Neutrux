package com.neutrux.api.NeutruxBlogSearchApi.service.implementation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxBlogSearchApi.repositories.BlogSearchRepository;
import com.neutrux.api.NeutruxBlogSearchApi.service.BlogSearchService;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.BlogElementDto;
import com.neutrux.api.NeutruxBlogSearchApi.shared.CategoryDto;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogElementEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogSearchApi.ui.models.CategoryEntity;

@Service
public class BlogSearchServiceImpl implements BlogSearchService {
	
	private BlogSearchRepository blogSearchRepository;
	
	@Autowired
	public BlogSearchServiceImpl(
		BlogSearchRepository blogSearchRepository
	) {
		this.blogSearchRepository = blogSearchRepository;
	}
	
	
	

	@Override
	public Set<BlogDto> getBlogs(int pageNumber, int pageLimit) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogDto> blogs = new HashSet<BlogDto>();
		
		Pageable blogsPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogEntity> blogsPage = this.blogSearchRepository.findAll(blogsPageable);
		
		blogs = this.getBlogDetails(blogsPage);
		return blogs;
	}

	@Override
	public Set<BlogDto> getBlogsByCategory(String category, int pageNumber, int pageLimit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BlogDto> getBlogsByTitleSubstring(String titleSubstr, int pageNumber, int pageLimit) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogDto> blogs = new HashSet<BlogDto>();
		
		Pageable blogPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogEntity> blogsPage = blogSearchRepository.findByTitleContains(titleSubstr, blogPageable);

		blogs = this.getBlogDetails(blogsPage);
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
	
	
	
	
	
	public Set<BlogDto> getBlogDetails( Page<BlogEntity> blogsPage ) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogDto> blogs = new HashSet<BlogDto>();
		Set<BlogElementEntity> elementList = new HashSet<BlogElementEntity>();
		Set<BlogElementDto> elementDtoList = new HashSet<BlogElementDto>();
		Iterator<BlogElementEntity> elementsIterator = null;
		BlogDto blogDto = null;
		BlogEntity blogEntity = null;
		BlogElementDto elementDto = null;
		BlogElementEntity element = null;
		CategoryDto categoryDto = null;
		CategoryEntity categoryEntity = null;
		String blogId = "";
		
		Iterator<BlogEntity> iterator = blogsPage.iterator();
		
		while(iterator.hasNext()) {
			blogEntity = iterator.next();
			elementList = blogEntity.getElements();
			elementsIterator = elementList.iterator();
			elementDtoList = new HashSet<BlogElementDto>();
			blogId = this.encryptId( blogEntity.getId() );
			
			while(elementsIterator.hasNext()) {
				element = elementsIterator.next();
				elementDto = mapper.map(element, BlogElementDto.class);
				elementDto.setElementId( this.encryptId(element.getId()) );
				elementDto.setBlogId(blogId);
				elementDtoList.add(elementDto);
			}

			blogDto = mapper.map(blogEntity, BlogDto.class);
			blogDto.setBlogId(blogId);
			blogDto.setElements(elementDtoList);
			
			categoryEntity = blogEntity.getCategory();
			categoryDto = mapper.map(categoryEntity, CategoryDto.class);
			categoryDto.setCategoryId( this.encryptId( categoryEntity.getId() ) );
			blogDto.setCategory(categoryDto);
			blogs.add(blogDto);
		}
		
		return blogs;
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