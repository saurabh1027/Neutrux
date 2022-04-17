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
	private CategoryDto category;
	private Set<BlogElementDto> elements;

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

}
