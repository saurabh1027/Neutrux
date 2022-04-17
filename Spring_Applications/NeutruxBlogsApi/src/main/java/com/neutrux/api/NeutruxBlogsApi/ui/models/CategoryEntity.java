package com.neutrux.api.NeutruxBlogsApi.ui.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name= "blog_categories")
public class CategoryEntity implements Serializable {

	private static final long serialVersionUID = 3052735862517903711L;

	@Id
	@GeneratedValue
	@Column(name= "category_id")
	private long id;
	
	@Column(nullable = false, unique = true)
	private String name;

	@Column
	private String description;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BlogEntity> blogs;
	
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

	public Set<BlogEntity> getBlogs() {
		return blogs;
	}

	public void setBlogs(Set<BlogEntity> blogs) {
		this.blogs = blogs;
	}
	
}