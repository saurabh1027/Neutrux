package com.neutrux.api.NeutruxBlogSearchApi.shared;

import java.io.Serializable;

public class CategoryDto implements Serializable {

	private static final long serialVersionUID = 1841963911915912677L;
	private String categoryId;
	private String name;
	private String description;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

}
