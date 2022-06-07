package com.neutrux.api.NeutruxUsersApi.ui.models.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BlogRequestModel {

	@NotNull(message = "Title cannot be null!")
	@Size(min = 50, max = 140, message = "Title should be greater than 50 and less than 140 characters!")
	private String title;

	@Size(min = 100, max = 200, message = "Description should be greater than 100 and less than 200 characters!")
	private String description;

	private String categoryId;

	private String thumbnail;

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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
