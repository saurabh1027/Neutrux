package com.neutrux.api.NeutruxBlogsApi.service.implementation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxBlogsApi.repositories.BlogElementsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.BlogsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.UsersRepository;
import com.neutrux.api.NeutruxBlogsApi.service.BlogElementsService;
import com.neutrux.api.NeutruxBlogsApi.service.BlogsService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogElementDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogElementEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

@Service
public class BlogElementsServiceImpl implements BlogElementsService {

	private BlogElementsRepository blogElementsRepository;
	private BlogsService blogsService;
	private BlogsRepository blogsRepository;
	private UsersRepository usersRepository;

	@Autowired
	public BlogElementsServiceImpl(BlogElementsRepository blogElementsRepository, BlogsService blogsService,
			BlogsRepository blogsRepository, UsersRepository usersRepository) {
		this.blogsService = blogsService;
		this.blogElementsRepository = blogElementsRepository;
		this.blogsRepository = blogsRepository;
		this.usersRepository = usersRepository;
	}

	@Override
	public Set<BlogElementDto> getElementsByBlogId(String blogId, String userId) {
		Set<BlogElementDto> elementDtoList = new HashSet<BlogElementDto>();
		BlogElementEntity elementEntity = null;
		BlogElementDto elementDto = null;
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Iterable<BlogElementEntity> iterable = blogElementsRepository.findAll();
		Iterator<BlogElementEntity> iterator = iterable.iterator();

		while (iterator.hasNext()) {
			elementEntity = iterator.next();
			elementDto = mapper.map(elementEntity, BlogElementDto.class);
			elementDto.setElementId(blogsService.encryptId(elementEntity.getId()));
			elementDto.setBlogId(blogId);
			elementDto.setUserId(userId);
			elementDtoList.add(elementDto);
		}

		return elementDtoList;
	}

	@Override
	public BlogElementDto getElementById(String elementId, String blogId) throws Exception {
		BlogElementDto blogElementDto = null;
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogElementEntity blogElementEntity = null;

		long id = blogsService.decryptId(elementId);
		try {
			blogElementEntity = blogElementsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Element with ID-" + elementId + " doesn't exists!");
		}

		blogElementDto = modelMapper.map(blogElementEntity, BlogElementDto.class);
		blogElementDto.setElementId(elementId);
		blogElementDto.setBlogId(blogId);
		blogElementDto.setUserId(this.encryptId(blogElementEntity.getUser().getId()));

		return blogElementDto;
	}

	@Override
	public BlogElementDto addElementToBlog(BlogElementDto blogElementDto) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogElementEntity blogElementEntity = null;
		BlogEntity blogEntity = null;
		UserEntity userEntity = null;

		long id = blogsService.decryptId( blogElementDto.getBlogId() );
		try {
			blogEntity = this.blogsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-"+ blogElementDto.getBlogId() + " doesn't exists!");
		}
		
		blogElementEntity = blogElementsRepository.findByPositionAndBlog(blogElementDto.getPosition(), blogEntity);
		
		if (blogElementEntity != null) {
			throw new Exception("Blog element with same position already exists!");
		}
		
		id = this.decryptId(blogElementDto.getUserId());
		try {
			userEntity = usersRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("User with ID-" + blogElementDto.getUserId() + " doesn't exists!");
		}

		blogElementEntity = mapper.map(blogElementDto, BlogElementEntity.class);
		blogElementEntity.setBlog(blogEntity);
		blogElementEntity.setUser(userEntity);

		blogElementEntity = blogElementsRepository.save(blogElementEntity);

		blogElementDto = mapper.map(blogElementEntity, BlogElementDto.class);
		blogElementDto.setBlogId(blogsService.encryptId(blogElementEntity.getBlog().getId()));
		blogElementDto.setUserId(blogsService.encryptId(blogElementEntity.getUser().getId()));
		blogElementDto.setElementId(blogsService.encryptId(blogElementEntity.getId()));

		return blogElementDto;
	}

	@Override
	public BlogElementDto updateElementById(BlogElementDto blogElementDto) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogEntity blogEntity = null;
		BlogElementEntity blogElementEntity = null;

		long id = blogsService.decryptId( blogElementDto.getBlogId() );
		try {
			blogEntity = this.blogsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-"+ blogElementDto.getBlogId() + " doesn't exists!");
		}
		
		blogElementEntity = blogElementsRepository.findByPositionAndBlog(blogElementDto.getPosition(), blogEntity);
		
		if (blogElementEntity != null) {
			throw new Exception("Blog element with same position already exists!");
		}

		id = blogsService.decryptId(blogElementDto.getElementId());
		try {
			blogElementEntity = blogElementsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-" + blogElementDto.getBlogId() + " doesn't exists!");
		}

		blogElementEntity.setDescription(blogElementDto.getDescription());
		blogElementEntity.setName(blogElementDto.getName());
		blogElementEntity.setValue(blogElementDto.getValue());
		blogElementEntity.setPosition(blogElementDto.getPosition());

		blogElementEntity = blogElementsRepository.save(blogElementEntity);

		blogElementDto = mapper.map(blogElementEntity, BlogElementDto.class);
		blogElementDto.setUserId(blogsService.encryptId(blogElementEntity.getUser().getId()));
		blogElementDto.setBlogId(blogsService.encryptId(blogElementEntity.getBlog().getId()));
		blogElementDto.setElementId(blogsService.encryptId(blogElementEntity.getId()));

		return blogElementDto;
	}

	@Override
	public void deleteElementById(String elementId) throws Exception {
		long id = this.blogsService.decryptId(elementId);

		BlogElementEntity blogElementEntity = null;
		try {
			blogElementEntity = this.blogElementsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Element with id: " + elementId + " doesn't exists!");
		}
		blogElementsRepository.delete(blogElementEntity);
	}

	public String encryptId(long id) {
		return (id * 673926356) + "";
	}

	public long decryptId(String blogId) throws Exception {
		long id = 0;
		try {
			id = Long.parseLong(blogId);
		} catch (NumberFormatException e) {
			throw new Exception("Element ID cannot be null!");
		}
		id = id / 673926356;
		return id;
	}

}
