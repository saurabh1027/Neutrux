package com.neutrux.api.NeutruxBlogsApi.ui.models.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.neutrux.api.NeutruxBlogsApi.shared.BlogCommentDto;

public class BlogResponseModel implements Serializable {

	private static final long serialVersionUID = 95371308421411824L;
	private String blogId;
	private String title;
	private String description;
	private Date creationDate;
	private String thumbnail;
	private CategoryResponseModel category;
	private Set<BlogElementResponseModel> elements;
	private Set<BlogImpressionResponseModel> impressions;
	private Set<BlogCommentDto> comments;
	private long impressionsCount;

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
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

	public CategoryResponseModel getCategory() {
		return category;
	}

	public void setCategory(CategoryResponseModel category) {
		this.category = category;
	}

	public Set<BlogElementResponseModel> getElements() {
		return elements;
	}

	public void setElements(Set<BlogElementResponseModel> elements) {
		this.elements = elements;
	}

	public Set<BlogImpressionResponseModel> getImpressions() {
		return impressions;
	}

	public void setImpressions(Set<BlogImpressionResponseModel> impressions) {
		this.impressions = impressions;
	}

	public long getImpressionsCount() {
		return impressionsCount;
	}

	public void setImpressionsCount(long impressionsCount) {
		this.impressionsCount = impressionsCount;
	}

	public Set<BlogCommentDto> getComments() {
		return comments;
	}

	public void setComments(Set<BlogCommentDto> comments) {
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
		return "BlogResponseModel [blogId=" + blogId + ", title=" + title + ", description=" + description
				+ ", creationDate=" + creationDate + ", thumbnail=" + thumbnail + ", category=" + category
				+ ", elements=" + elements + ", impressions=" + impressions + ", comments=" + comments
				+ ", impressionsCount=" + impressionsCount + "]";
	}

}