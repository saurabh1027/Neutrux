package com.neutrux.api.NeutruxBlogsApi.ui.models.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryRequestModel {

	@NotNull(message = "Category name cannot be null!")
	@Size(min=2, max=20, message = "Category should be greater than 2 and less than 20 characters")
	private String name;
	
	@NotNull(message = "Category description cannot be null!")
	private String description;

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
