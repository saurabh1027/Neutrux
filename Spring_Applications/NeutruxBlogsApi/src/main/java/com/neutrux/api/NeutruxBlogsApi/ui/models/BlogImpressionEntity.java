package com.neutrux.api.NeutruxBlogsApi.ui.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table
@Entity(name = "blog_impressions")
public class BlogImpressionEntity implements Serializable {

	private static final long serialVersionUID = 4028603156173297543L;

	@Id
	@GeneratedValue
	@Column(name = "blog_impression_id")
	@JsonIgnore
	private long id;

	@Column(nullable = false)
	private String type;

	@ManyToOne
	@JoinColumn(name = "blog_id", nullable = false)
	@JsonIgnore
	private BlogEntity blog;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserEntity user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BlogEntity getBlog() {
		return blog;
	}

	public void setBlog(BlogEntity blog) {
		this.blog = blog;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
