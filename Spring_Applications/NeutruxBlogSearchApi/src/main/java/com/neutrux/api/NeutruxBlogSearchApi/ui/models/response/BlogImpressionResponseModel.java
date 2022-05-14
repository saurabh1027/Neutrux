package com.neutrux.api.NeutruxBlogSearchApi.ui.models.response;

import java.io.Serializable;

public class BlogImpressionResponseModel implements Serializable {

	private static final long serialVersionUID = 1105399224356563011L;

	private String impressionId;
	private String type;
	private String blogId;
	private String userId;

	public String getImpressionId() {
		return impressionId;
	}

	public void setImpressionId(String impressionId) {
		this.impressionId = impressionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
