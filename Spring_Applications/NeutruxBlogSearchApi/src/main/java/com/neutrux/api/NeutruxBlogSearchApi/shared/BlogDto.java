package com.neutrux.api.NeutruxBlogSearchApi.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class BlogDto implements Serializable {

	private static final long serialVersionUID = 4861507615446876825L;
	private String blogId;
	private String title;
	private String description;
	private Date creationDate;
	private String thumbnail;
	private CategoryDto category;
	private UserDto user;
	private Set<BlogElementDto> elements;
	private Set<BlogImpressionDto> impressions;
	private Set<BlogCommentDto> comments;
	private long impressionsCount;

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public Set<BlogElementDto> getElements() {
		return elements;
	}

	public void setElements(Set<BlogElementDto> elements) {
		this.elements = elements;
	}

	public Set<BlogImpressionDto> getImpressions() {
		return impressions;
	}

	public void setImpressions(Set<BlogImpressionDto> impressions) {
		this.impressions = impressions;
	}

	public Set<BlogCommentDto> getComments() {
		return comments;
	}

	public void setComments(Set<BlogCommentDto> comments) {
		this.comments = comments;
	}

	public long getImpressionsCount() {
		return impressionsCount;
	}

	public void setImpressionsCount(long impressionsCount) {
		this.impressionsCount = impressionsCount;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

}
