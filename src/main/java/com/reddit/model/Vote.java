package com.reddit.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long voteId;
    private VoteType voteType;
    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    
//	public Vote(Long voteId, VoteType voteType, @NotNull Post post, User user) {
//		super();
//		this.voteId = voteId;
//		this.voteType = voteType;
//		this.post = post;
//		this.user = user;
//	}
//    
//	public Vote() {
//		
//	}
//
//	public Long getVoteId() {
//		return voteId;
//	}
//
//	public void setVoteId(Long voteId) {
//		this.voteId = voteId;
//	}
//
//	public VoteType getVoteType() {
//		return voteType;
//	}
//
//	public void setVoteType(VoteType voteType) {
//		this.voteType = voteType;
//	}
//
//	public Post getPost() {
//		return post;
//	}
//
//	public void setPost(Post post) {
//		this.post = post;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
    
    
    
    
}