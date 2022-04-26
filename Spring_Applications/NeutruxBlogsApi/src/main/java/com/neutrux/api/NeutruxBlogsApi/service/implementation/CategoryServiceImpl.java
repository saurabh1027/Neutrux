package com.neutrux.api.NeutruxBlogsApi.service.implementation;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
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

	@Override
	public CategoryDto getCategoryById(String categoryId) throws Exception {
		CategoryEntity categoryEntity = null;
		CategoryDto categoryDto = null;
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		long id = this.decryptId(categoryId);
		categoryEntity = this.categoryRepository.findById(id).get();
		categoryDto = mapper.map(categoryEntity, CategoryDto.class);
		categoryDto.setCategoryId(categoryId);
		return categoryDto;
	}
	
	@Override
	public CategoryDto addCategory(CategoryDto categoryDto) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		CategoryEntity categoryEntity = mapper.map(categoryDto, CategoryEntity.class);
		
		try {
			categoryEntity = this.categoryRepository.save(categoryEntity);
		} catch (DataIntegrityViolationException e) {
			// Exception Handling for Duplicate Entry of Email
			if (e.getRootCause() != null
					&& e.getRootCause().getClass().equals(SQLIntegrityConstraintViolationException.class)) {
				SQLIntegrityConstraintViolationException ex = (SQLIntegrityConstraintViolationException) e
						.getRootCause();
				if (ex.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
					throw new Exception("Category with same name already exists!");
				}
			}
		}
		
		categoryDto.setCategoryId( this.encryptId( categoryEntity.getId() ) );
		
		return categoryDto;
	}
	
	@Override
	public CategoryDto updateCategory(CategoryDto newCategory) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		CategoryEntity categoryEntity = null;
		long id = this.decryptId( newCategory.getCategoryId() );
		
		categoryEntity = this.categoryRepository.findById( id ).get();
		
		categoryEntity.setDescription( newCategory.getDescription() );
		
		categoryEntity = categoryRepository.save(categoryEntity);
		
		CategoryDto updatedCategory = mapper.map(categoryEntity, CategoryDto.class);
		updatedCategory.setCategoryId( newCategory.getCategoryId() );
		return updatedCategory;
	}
	
	@Override
	public void deleteCategory(String categoryId) throws Exception {
		long id = this.decryptId(categoryId);
		CategoryEntity categoryEntity = this.categoryRepository.findById(id).get();
		this.categoryRepository.delete(categoryEntity);
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
