package com.neutrux.api.NeutruxBlogSearchApi.shared;

import java.io.Serializable;
import java.util.Date;

public class BlogCommentDto implements Serializable {

	private static final long serialVersionUID = -379042518933394248L;
	private String commentId;
	private String content;
	private Date creationDate;
	private String blogId;
	private UserDto user;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

}