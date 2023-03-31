package com.neutrux.api.NeutruxBlogsApi.ui.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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
	
	@Column(nullable = false)
	private String thumbnail;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	@JsonIgnore
	private CategoryEntity category;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserEntity user;

	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BlogElementEntity> elements;
	
	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BlogImpressionEntity> impressions;
	
	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BlogCommentEntity> comments;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Set<BlogElementEntity> getElements() {
		return elements;
	}

	public void setElements(Set<BlogElementEntity> elements) {
		this.elements = elements;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public Set<BlogImpressionEntity> getImpressions() {
		return impressions;
	}

	public void setImpressions(Set<BlogImpressionEntity> impressions) {
		this.impressions = impressions;
	}

	public Set<BlogCommentEntity> getComments() {
		return comments;
	}

	public void setComments(Set<BlogCommentEntity> comments) {
		this.comments = comments;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return "BlogEntity [id=" + id + ", title=" + title + ", description=" + description + ", creationDate="
				+ creationDate + ", thumbnail=" + thumbnail + ", category=" + category + ", user=" + user
				+ ", elements=" + elements + ", impressions=" + impressions + ", comments=" + comments + "]";
	}
	
}
