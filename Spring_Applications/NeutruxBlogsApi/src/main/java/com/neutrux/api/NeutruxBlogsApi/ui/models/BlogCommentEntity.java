package com.neutrux.api.NeutruxBlogsApi.ui.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name= "blog_comments")
public class BlogCommentEntity implements Serializable {
	
	private static final long serialVersionUID = 8437191277199267592L;

	@Id
	@GeneratedValue
	@Column(name = "blog_comment_id")
	private long id;
	
	@Column(nullable = false)
	private String content;
	
	@Column
	private Date creationDate;
	
	@ManyToOne
	@JoinColumn(name = "blog_id", nullable = false)
	private BlogEntity blog;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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