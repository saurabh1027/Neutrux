package com.neutrux.api.NeutruxBlogsApi.ui.models.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BlogImpressionRequestModel implements Serializable {

	private static final long serialVersionUID = 4278080737666610739L;
	@NotNull(message= "Impression type cannot be null!")
	@Size(min=2, max=20, message= "Impression type should be 2 characters short and 20 characters long!")
	private String type;
	
	@NotNull(message= "User Id cannot be null!")
	private String userId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
