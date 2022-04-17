package com.neutrux.api.NeutruxUsersApi.ui.models.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BlogRequestModel {

	@NotNull(message = "Title cannot be null!")
	@Size(min = 20, max = 80, message = "Title should be greater than 20 and less than 80 characters!")
	private String title;

	@Size(min = 30, max = 150, message = "Description should be greater than 30 and less than 150 characters!")
	private String description;

	private String categoryId;

	@NotNull(message = "User ID cannot be null!")
	private String userId;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
