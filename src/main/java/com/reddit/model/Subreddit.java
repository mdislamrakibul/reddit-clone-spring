package com.reddit.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Subreddit {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @NotBlank(message = "Community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @OneToMany(fetch = LAZY)
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)
    private User user;
    
//    public Subreddit() {
//		
//	}
//    
//    
//	public Subreddit(Long id, @NotBlank(message = "Community name is required") String name,
//			@NotBlank(message = "Description is required") String description, List<Post> posts, Instant createdDate,
//			User user) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.description = description;
//		this.posts = posts;
//		this.createdDate = createdDate;
//		this.user = user;
//	}
//	/**
//	 * @return the id
//	 */
//	public Long getId() {
//		return id;
//	}
//	/**
//	 * @param id the id to set
//	 */
//	public void setId(Long id) {
//		this.id = id;
//	}
//	/**
//	 * @return the name
//	 */
//	public String getName() {
//		return name;
//	}
//	/**
//	 * @param name the name to set
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
//	/**
//	 * @return the description
//	 */
//	public String getDescription() {
//		return description;
//	}
//	/**
//	 * @param description the description to set
//	 */
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	/**
//	 * @return the posts
//	 */
//	public List<Post> getPosts() {
//		return posts;
//	}
//	/**
//	 * @param posts the posts to set
//	 */
//	public void setPosts(List<Post> posts) {
//		this.posts = posts;
//	}
//	/**
//	 * @return the createdDate
//	 */
//	public Instant getCreatedDate() {
//		return createdDate;
//	}
//	/**
//	 * @param createdDate the createdDate to set
//	 */
//	public void setCreatedDate(Instant createdDate) {
//		this.createdDate = createdDate;
//	}
//	/**
//	 * @return the user
//	 */
//	public User getUser() {
//		return user;
//	}
//	/**
//	 * @param user the user to set
//	 */
//	public void setUser(User user) {
//		this.user = user;
//	}
    
    
}