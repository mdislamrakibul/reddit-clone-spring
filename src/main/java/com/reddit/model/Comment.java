package com.reddit.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @NotEmpty
    private String text;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    
    
//    public Comment() {
//		
//	}
//    
//    
//	public Comment(Long id, @NotEmpty String text, Post post, Instant createdDate, User user) {
//		super();
//		this.id = id;
//		this.text = text;
//		this.post = post;
//		this.createdDate = createdDate;
//		this.user = user;
//	}
//	
//	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getText() {
//		return text;
//	}
//	public void setText(String text) {
//		this.text = text;
//	}
//	public Post getPost() {
//		return post;
//	}
//	public void setPost(Post post) {
//		this.post = post;
//	}
//	public Instant getCreatedDate() {
//		return createdDate;
//	}
//	public void setCreatedDate(Instant createdDate) {
//		this.createdDate = createdDate;
//	}
//	public User getUser() {
//		return user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
    
    
}