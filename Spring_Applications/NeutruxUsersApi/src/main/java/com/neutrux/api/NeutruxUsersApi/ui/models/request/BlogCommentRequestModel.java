package com.neutrux.api.NeutruxUsersApi.ui.models.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class BlogCommentRequestModel implements Serializable {

	private static final long serialVersionUID = 2263285838648349906L;

	@NotNull(message = "Content of the comment cannot be null!")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
