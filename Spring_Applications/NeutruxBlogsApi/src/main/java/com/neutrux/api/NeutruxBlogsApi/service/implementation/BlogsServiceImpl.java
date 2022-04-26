package com.neutrux.api.NeutruxBlogsApi.service.implementation;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
import com.neutrux.api.NeutruxBlogsApi.repositories.BlogsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.CategoryRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.UsersRepository;
import com.neutrux.api.NeutruxBlogsApi.service.BlogsService;
import com.neutrux.api.NeutruxBlogsApi.service.UsersService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogDto;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogElementDto;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogImpressionDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogElementEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogImpressionEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.CategoryEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

@Service
public class BlogsServiceImpl implements BlogsService {

	private BlogsRepository blogsRepository;
	private UsersService usersService;
	private UsersRepository usersRepository;
	private CategoryRepository categoryRepository;

	@Autowired
	public BlogsServiceImpl(BlogsRepository blogsRepository, UsersRepository usersRepository, UsersService usersService,
			CategoryRepository categoryRepository) {
		this.blogsRepository = blogsRepository;
		this.usersService = usersService;
		this.usersRepository = usersRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Set<BlogDto> getBlogsByUserId(String userId, boolean includeImpressions, int pageNumber, int pageLimit)
			throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogDto blogDto = null;
		BlogElementDto blogElementDto = null;
		Set<BlogElementDto> blogElementDtos = new HashSet<BlogElementDto>();
		Set<BlogDto> blogs = new HashSet<BlogDto>();
		UserEntity userEntity = null;
		BlogEntity blogEntity = null;

		long id = this.decryptId(userId);
		try {
			userEntity = usersRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("User with ID-" + userId + " doesn't exists!");
		}

		Pageable blogPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogEntity> blogPages = blogsRepository.findAllByUser(userEntity, blogPageable);

		Iterator<BlogEntity> iterator = blogPages.iterator();

		while (iterator.hasNext()) {
			blogEntity = iterator.next();
			String blogId = encryptId(blogEntity.getId());
			blogDto = this.getBlogDetails(blogEntity, blogId, includeImpressions);
			blogDto.setCategoryId(this.encryptId(blogEntity.getCategory().getId()));

			Iterator<BlogElementDto> elementsIterator = blogDto.getElements().iterator();
			while (elementsIterator.hasNext()) {
				blogElementDto = elementsIterator.next();
				blogElementDto.setUserId(userId);
				blogElementDtos.add(blogElementDto);
			}
			blogDto.setElements(blogElementDtos);

			blogs.add(blogDto);
		}

		return blogs;
	}

	@Override
	public BlogDto createBlog(BlogDto blogDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = null;
		CategoryEntity categoryEntity = null;
		BlogEntity blogEntity = null;
		BlogDto createdBlog = null;

		blogEntity = modelMapper.map(blogDto, BlogEntity.class);
		blogEntity.setCreationDate(new Date());

		long id = usersService.decryptUserId(blogDto.getUserId());
		try {
			userEntity = usersRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("User with ID-" + id + " doesn't exists!");
		}

		id = usersService.decryptUserId(blogDto.getCategoryId());
		try {
			categoryEntity = categoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("category with ID-" + id + " not found!");
		}

		blogEntity.setUser(userEntity);
		blogEntity.setCategory(categoryEntity);

		try {
			blogEntity = blogsRepository.save(blogEntity);
		} catch (DataIntegrityViolationException e) {
			// Exception Handling for Duplicate Entry of Email
			if (e.getRootCause() != null
					&& e.getRootCause().getClass().equals(SQLIntegrityConstraintViolationException.class)) {
				SQLIntegrityConstraintViolationException ex = (SQLIntegrityConstraintViolationException) e
						.getRootCause();
				if (ex.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
					throw new Exception("Blog with same title already exists!");
				}
			}
		}

		String blogId = encryptId(blogEntity.getId());

		createdBlog = modelMapper.map(blogEntity, BlogDto.class);
		createdBlog.setBlogId(blogId);
		createdBlog.setUserId(blogDto.getUserId());
		createdBlog.setElements(new HashSet<BlogElementDto>());
		createdBlog.setCategoryId(blogDto.getCategoryId());

		return createdBlog;
	}

	@Override
	public BlogDto getBlogByBlogId(String blogId, String userId, boolean includeImpressions) throws Exception {
		BlogEntity blogEntity = null;
		BlogElementDto blogElementDto = null;
		Set<BlogElementDto> blogElementDtos = new HashSet<BlogElementDto>();

		long id = decryptId(blogId);
		try {
			blogEntity = blogsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog doesn't exists!");
		}

		BlogDto blogDto = this.getBlogDetails(blogEntity, blogId, includeImpressions);
		blogDto.setUserId(this.encryptId(blogEntity.getUser().getId()));
		blogDto.setCategoryId(this.encryptId(blogEntity.getCategory().getId()));

		Iterator<BlogElementDto> elementsIterator = blogDto.getElements().iterator();
		while (elementsIterator.hasNext()) {
			blogElementDto = elementsIterator.next();
			blogElementDto.setUserId(userId);
			blogElementDtos.add(blogElementDto);
		}
		blogDto.setElements(blogElementDtos);

		return blogDto;
	}

	@Override
	public BlogDto updateBlog(BlogDto newBlogDetails) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		CategoryEntity categoryEntity = null;

		long blogId = this.decryptId(newBlogDetails.getBlogId());
		BlogEntity oldBlogEntity = blogsRepository.findById(blogId).get();
		oldBlogEntity.setTitle(newBlogDetails.getTitle());
		oldBlogEntity.setDescription(newBlogDetails.getDescription());

		String categoryId = newBlogDetails.getCategoryId();
		if (categoryId != null) {
			long id = this.decryptId(newBlogDetails.getCategoryId());
			try {
				categoryEntity = categoryRepository.findById(id).get();
			} catch (NoSuchElementException e) {
				throw new Exception("category with ID-" + id + " not found!");
			}
			oldBlogEntity.setCategory(categoryEntity);
		}

		BlogEntity newBlogEntity = oldBlogEntity;
		newBlogEntity = blogsRepository.save(newBlogEntity);

		BlogDto updatedBlogDetails = modelMapper.map(newBlogEntity, BlogDto.class);

		String newBlogId = this.encryptId(newBlogEntity.getId());
		updatedBlogDetails.setBlogId(newBlogId);
		updatedBlogDetails.setUserId(newBlogDetails.getUserId());
		updatedBlogDetails.setCategoryId(categoryId);

		return updatedBlogDetails;
	}

	@Override
	public void deleteBlogByBlogId(String blogId) throws Exception {
		long id = this.decryptId(blogId);

		try {
			this.blogsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with id: " + blogId + " doesn't exists!");
		}

		blogsRepository.deleteById(id);
	}

	@Override
	public String encryptId(long id) {
		return (id * 673926356) + "";
	}

	@Override
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

	public BlogDto getBlogDetails(BlogEntity blogEntity, String blogId, boolean includeImpressions) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogImpressionDto> impressionDtoList = new HashSet<BlogImpressionDto>();

		Set<BlogElementEntity> elementList = blogEntity.getElements();
		Iterator<BlogElementEntity> elementsIterator = elementList.iterator();
		Set<BlogElementDto> elementDtoList = new HashSet<BlogElementDto>();

		while (elementsIterator.hasNext()) {
			BlogElementEntity element = elementsIterator.next();
			BlogElementDto elementDto = modelMapper.map(element, BlogElementDto.class);
			elementDto.setElementId(this.encryptId(element.getId()));
			elementDto.setBlogId(blogId);
			elementDtoList.add(elementDto);
		}

		if( includeImpressions ) {
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
		blogDto.setBlogId(blogId);
		blogDto.setElements(elementDtoList);
		blogDto.setImpressions(impressionDtoList);
		blogDto.setImpressionsCount( blogEntity.getImpressions().size() );
		
		return blogDto;
	}

}
