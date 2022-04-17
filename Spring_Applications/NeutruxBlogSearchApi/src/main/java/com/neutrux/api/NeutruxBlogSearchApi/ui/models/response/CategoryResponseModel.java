package com.neutrux.api.NeutruxBlogSearchApi.ui.models.response;

import java.io.Serializable;

public class CategoryResponseModel implements Serializable {

	private static final long serialVersionUID = -6484247303361437122L;
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
