package com.neutrux.api.NeutruxBlogsApi.service.implementation;

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

import com.neutrux.api.NeutruxBlogsApi.repositories.CategoryRepository;
import com.neutrux.api.NeutruxBlogsApi.service.CategoryService;
import com.neutrux.api.NeutruxBlogsApi.shared.CategoryDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.CategoryEntity;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	private CategoryRepository categoryRepository;
	
	@Autowired
	public CategoryServiceImpl(
		CategoryRepository categoryRepository
	) {
		this.categoryRepository = categoryRepository;
	}
	

	@Override
	public Set<CategoryDto> getCategories(int pageNumber, int pageLimit) {
		Set<CategoryDto> categoryDtos = new HashSet<CategoryDto>();
		CategoryEntity categoryEntity = null;
		CategoryDto categoryDto = null;
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Pageable categoryPageable = PageRequest.of(pageNumber, pageLimit);
		Page<CategoryEntity> categoryPages = this.categoryRepository.findAll(categoryPageable);
		Iterator<CategoryEntity> iterator = categoryPages.iterator();
		
		while(iterator.hasNext()) {
			categoryEntity = iterator.next();
			categoryDto = mapper.map(categoryEntity, CategoryDto.class);
			String categoryId = this.encryptId( categoryEntity.getId() );
			categoryDto.setCategoryId(categoryId);
			categoryDtos.add(categoryDto);
		}
		
		return categoryDtos;
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
