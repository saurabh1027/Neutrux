package com.neutrux.api.NeutruxBlogSearchApi.ui.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "blog_elements")
@Entity
public class BlogElementEntity implements Serializable {

	private static final long serialVersionUID = 2262452039393545955L;

	@Id
	@GeneratedValue
	@Column(name = "blog_element_id")
	@JsonIgnore
	private long id;

	@Column(nullable = false)
	private String name;

	@Column
	private String description;

	@Column(nullable = false)
	@Lob
	private String value;

	@Column(nullable = false)
	private long position;

	@ManyToOne
	@JoinColumn(name = "blog_id", nullable = false)
	@JsonIgnore
	private BlogEntity blog;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public BlogEntity getBlog() {
		return blog;
	}

	public void setBlog(BlogEntity blog) {
		this.blog = blog;
	}

}
