package com.neutrux.api.NeutruxBlogsApi.ui.models.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BlogCommentRequestModel implements Serializable {

	private static final long serialVersionUID = 2263285838648349906L;

	@NotNull(message = "Content of the comment cannot be null!")
	@Size(min = 20, max = 100, message = "Content should be 20 characters small & 100 characters long!")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
