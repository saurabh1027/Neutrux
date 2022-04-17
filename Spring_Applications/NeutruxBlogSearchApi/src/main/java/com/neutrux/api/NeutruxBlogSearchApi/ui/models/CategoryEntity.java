package com.neutrux.api.NeutruxBlogSearchApi.ui.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "blog_categories")
public class CategoryEntity {

	@Id
	@GeneratedValue
	@Column(name= "category_id")
	private long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column
	private String description;
	
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
	
}