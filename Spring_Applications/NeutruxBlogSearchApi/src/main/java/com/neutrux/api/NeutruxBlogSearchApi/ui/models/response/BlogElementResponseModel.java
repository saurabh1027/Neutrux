package com.neutrux.api.NeutruxBlogSearchApi.ui.models.response;

import java.io.Serializable;

public class BlogElementResponseModel implements Serializable {

	private static final long serialVersionUID = 6754330266580241465L;
	private String elementId;
	private String name;
	private String description;
	private String value;
	private long position;
	private String blogId;

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

}