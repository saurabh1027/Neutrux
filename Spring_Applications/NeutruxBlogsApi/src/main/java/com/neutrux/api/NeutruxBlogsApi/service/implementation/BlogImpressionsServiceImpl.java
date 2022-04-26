package com.neutrux.api.NeutruxBlogsApi.service.implementation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neutrux.api.NeutruxBlogsApi.repositories.BlogImpressionsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.BlogsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.UsersRepository;
import com.neutrux.api.NeutruxBlogsApi.service.BlogImpressionsService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogImpressionDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogImpressionEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

@Service
public class BlogImpressionsServiceImpl implements BlogImpressionsService {
	
	private BlogImpressionsRepository blogImpressionsRepository;
	private BlogsRepository blogsRepository;
	private UsersRepository usersRepository;
	
	@Autowired
	public BlogImpressionsServiceImpl(
		BlogImpressionsRepository blogImpressionsRepository,
		BlogsRepository blogsRepository,
		UsersRepository usersRepository
	) {
		this.blogImpressionsRepository = blogImpressionsRepository;
		this.blogsRepository = blogsRepository;
		this.usersRepository = usersRepository;
	}

	

	@Override
	public long getBlogImpressionsCount(String blogIdStr) throws Exception {
		BlogEntity blogEntity = null;
		long blogId = decryptId(blogIdStr);
		
		try {
			blogEntity = blogsRepository.findById( blogId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-"+blogId+" doesn't exists!");
		}
		
		return this.blogImpressionsRepository.countByBlog(blogEntity);
	}
	
	@Override
	public Set<BlogImpressionDto> getImpressionsByBlogId(String blogId) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Set<BlogImpressionDto> blogImpressionDtos = new HashSet<BlogImpressionDto>();
		Set<BlogImpressionEntity> blogImpressionEntities = new HashSet<BlogImpressionEntity>();
		BlogEntity blogEntity = null;
		BlogImpressionEntity blogImpressionEntity = null;
		BlogImpressionDto blogImpressionDto = null;
		
		long id = decryptId(blogId);
		try {
			blogEntity = blogsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-"+blogId+" doesn't exists!");
		}
		
		blogImpressionEntities = this.blogImpressionsRepository.findByBlog(blogEntity);
		Iterator<BlogImpressionEntity> iterator = blogImpressionEntities.iterator();
		while( iterator.hasNext() ) {
			blogImpressionEntity = iterator.next();
			blogImpressionDto = mapper.map(blogImpressionEntity, BlogImpressionDto.class);
			blogImpressionDto.setImpressionId( this.encryptId(blogImpressionEntity.getId()) );
			blogImpressionDto.setBlogId( this.encryptId( blogImpressionEntity.getBlog().getId() ) );
			blogImpressionDto.setUserId( this.encryptId( blogImpressionEntity.getUser().getId() ) );
			blogImpressionDtos.add(blogImpressionDto);
		}
		
		return blogImpressionDtos;
	}
	
	@Override
	public BlogImpressionDto addOrUpdateImpressionToBlog(BlogImpressionDto blogImpressionDto) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		long blogId = this.decryptId( blogImpressionDto.getBlogId() );
		long userId = this.decryptId( blogImpressionDto.getUserId() );
		BlogImpressionEntity blogImpressionEntity = null;
		UserEntity userEntity = null;
		BlogEntity blogEntity = null;
		
		try {
			userEntity = this.usersRepository.findById( userId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("User with ID"+blogImpressionDto.getUserId()+" not found!");
		}
		try {
			blogEntity = this.blogsRepository.findById( blogId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID"+blogImpressionDto.getBlogId()+" not found!");
		}
		blogImpressionEntity = this.blogImpressionsRepository.findByBlogAndUser(blogEntity, userEntity);
		
		if(blogImpressionEntity != null) {
			blogImpressionEntity.setType( blogImpressionDto.getType() );
		} else {
			blogImpressionEntity = mapper.map(blogImpressionDto, BlogImpressionEntity.class);
			blogImpressionEntity.setBlog(blogEntity);
			blogImpressionEntity.setUser(userEntity);
		}
		
		blogImpressionEntity = this.blogImpressionsRepository.save( blogImpressionEntity );
		
		blogImpressionDto.setImpressionId( this.encryptId( blogImpressionEntity.getId() ) );
		return blogImpressionDto;
	}
	
	@Override
	public void removeImpressionFromBlog(String blogIdStr, String userIdStr) throws Exception {
		long blogId = this.decryptId( blogIdStr );
		long userId = this.decryptId( userIdStr );
		
		UserEntity userEntity = null;
		BlogEntity blogEntity = null;
		
		try {
			userEntity = this.usersRepository.findById( userId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("User with ID"+userIdStr+" not found!");
		}
		try {
			blogEntity = this.blogsRepository.findById( blogId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID"+blogIdStr+" not found!");
		}
		
		BlogImpressionEntity blogImpressionEntity = this.blogImpressionsRepository.findByBlogAndUser(blogEntity, userEntity);
		if( blogImpressionEntity != null ) {
			this.blogImpressionsRepository.delete( blogImpressionEntity );
		} else {
			throw new Exception("Impression with same Blog and User doesn't exists!");
		}
		
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