package com.neutrux.api.NeutruxUsersApi.ui.models.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class BlogResponseModel implements Serializable {

	private static final long serialVersionUID = 95371308421411824L;
	private String blogId;
	private String title;
	private String description;
	private Date creationDate;
	private Set<BlogElementResponseModel> elements;

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

	public Set<BlogElementResponseModel> getElements() {
		return elements;
	}

	public void setElements(Set<BlogElementResponseModel> elements) {
		this.elements = elements;
	}

}