package com.neutrux.api.NeutruxBlogsApi.service.implementation;

import java.util.Date;
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

import com.neutrux.api.NeutruxBlogsApi.repositories.BlogCommentsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.BlogsRepository;
import com.neutrux.api.NeutruxBlogsApi.repositories.UsersRepository;
import com.neutrux.api.NeutruxBlogsApi.service.BlogCommentsService;
import com.neutrux.api.NeutruxBlogsApi.shared.BlogCommentDto;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogCommentEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.BlogEntity;
import com.neutrux.api.NeutruxBlogsApi.ui.models.UserEntity;

@Service
public class BlogCommentsServiceImpl implements BlogCommentsService {
	
	private BlogCommentsRepository blogCommentsRepository;
	private BlogsRepository blogsRepository;
	private UsersRepository usersRepository;

	@Autowired
	public BlogCommentsServiceImpl(
		BlogCommentsRepository blogCommentsRepository,
		BlogsRepository blogsRepository,
		UsersRepository usersRepository
	) {
		this.blogCommentsRepository = blogCommentsRepository;
		this.blogsRepository = blogsRepository;
		this.usersRepository = usersRepository;
	}
	
	
	@Override
	public Set<BlogCommentDto> getCommentsOfBlog(String blogIdStr, int pageNumber, int pageLimit) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		long blogId = this.decryptId( blogIdStr );
		BlogCommentEntity blogCommentEntity = null;
		BlogCommentDto blogCommentDto = null;
		Set<BlogCommentDto> comments = new HashSet<BlogCommentDto>();
		
		BlogEntity blogEntity = null;
		try {
			blogEntity = blogsRepository.findById( blogId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-" + blogIdStr + " doesn't exists!");
		}
		
		Pageable commentPageable = PageRequest.of(pageNumber, pageLimit);
		Page<BlogCommentEntity> commentEntities = this.blogCommentsRepository.findAllByBlog( blogEntity, commentPageable );
		Iterator<BlogCommentEntity> iterator = commentEntities.iterator();
		while(iterator.hasNext()) {
			blogCommentEntity = iterator.next();
			blogCommentDto = mapper.map(blogCommentEntity, BlogCommentDto.class);
			blogCommentDto.setCommentId( this.encryptId(blogCommentEntity.getId()) );
			blogCommentDto.setBlogId( this.encryptId(blogCommentEntity.getBlog().getId()) );
			blogCommentDto.setUserId( this.encryptId(blogCommentEntity.getUser().getId()) );
			comments.add(blogCommentDto);
		}
		
		return comments;
	}
	
	@Override
	public BlogCommentDto getCommentByCommentId(String commentIdStr) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		long commentId = this.decryptId(commentIdStr);

		BlogCommentEntity blogCommentEntity = null;
		BlogCommentDto blogCommentDto = null;
		try {
			blogCommentEntity = blogCommentsRepository.findById( commentId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Comment with ID-" + commentIdStr + " doesn't exists!");
		}
		
		blogCommentDto = mapper.map(blogCommentEntity, BlogCommentDto.class);
		blogCommentDto.setCommentId( this.encryptId(blogCommentEntity.getId()) );
		blogCommentDto.setBlogId( this.encryptId(blogCommentEntity.getBlog().getId()) );
		blogCommentDto.setUserId( this.encryptId(blogCommentEntity.getUser().getId()) );
		
		return blogCommentDto;
	}
	
	@Override
	public BlogCommentDto addCommentToBlog(BlogCommentDto blogCommentDto) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BlogCommentEntity blogCommentEntity = mapper.map(blogCommentDto, BlogCommentEntity.class);
		long blogId = this.decryptId( blogCommentDto.getBlogId() );
		long userId = this.decryptId( blogCommentDto.getUserId() );
		BlogEntity blogEntity = null;
		UserEntity userEntity = null;
		
		try {
			userEntity = usersRepository.findById( userId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("User with ID-" + userId + " doesn't exists!");
		}
		try {
			blogEntity = blogsRepository.findById( blogId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Blog with ID-" + blogId + " doesn't exists!");
		}
		
		blogCommentEntity.setBlog(blogEntity);
		blogCommentEntity.setUser(userEntity);
		blogCommentEntity.setCreationDate( new Date() );
		
		blogCommentEntity = this.blogCommentsRepository.save( blogCommentEntity );
		
		BlogCommentDto addedComment = mapper.map(blogCommentEntity, BlogCommentDto.class);
		addedComment.setCommentId( this.encryptId( blogCommentEntity.getId() ) );
		addedComment.setBlogId( blogCommentDto.getBlogId() );
		addedComment.setUserId( blogCommentDto.getUserId() );
		return addedComment;
	}

	@Override
	public BlogCommentDto updateCommentToBlog(BlogCommentDto blogCommentDto) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		long commentId = this.decryptId( blogCommentDto.getCommentId() );
		BlogCommentEntity blogCommentEntity = null;
		try {
			blogCommentEntity = this.blogCommentsRepository.findById( commentId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Comment with ID-" + commentId + " doesn't exists!");
		}
		
		blogCommentEntity.setContent( blogCommentDto.getContent() );
		blogCommentEntity = this.blogCommentsRepository.save( blogCommentEntity );
		
		BlogCommentDto addedComment = mapper.map(blogCommentEntity, BlogCommentDto.class);
		addedComment.setCommentId( blogCommentDto.getCommentId() );
		addedComment.setBlogId( this.encryptId( blogCommentEntity.getBlog().getId() ) );
		addedComment.setUserId( this.encryptId( blogCommentEntity.getUser().getId() ) );
		return addedComment;
	}
	
	@Override
	public void deleteCommentByCommentId(String commentIdStr) throws Exception {
		long commentId = this.decryptId( commentIdStr );
		
		BlogCommentEntity blogCommentEntity = null;
		try {
			blogCommentEntity = this.blogCommentsRepository.findById( commentId ).get();
		} catch (NoSuchElementException e) {
			throw new Exception("Comment with ID-" + commentId + " doesn't exists!");
		}
		
		this.blogCommentsRepository.delete(blogCommentEntity);
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
