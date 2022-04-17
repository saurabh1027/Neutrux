package com.neutrux.api.NeutruxBlogSearchApi.ui.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "blogs")
public class BlogEntity implements Serializable {

	private static final long serialVersionUID = -3103933975503318030L;

	@Id
	@GeneratedValue
	@Column(name = "blog_id")
	private long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column
	private String description;

	@Column(nullable = false)
	private Date creationDate;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	@JsonIgnore
	private CategoryEntity category;

	@OneToMany(mappedBy = "blog")
	private Set<BlogElementEntity> elements;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<BlogElementEntity> getElements() {
		return elements;
	}

	public void setElements(Set<BlogElementEntity> elements) {
		this.elements = elements;
	}

}
