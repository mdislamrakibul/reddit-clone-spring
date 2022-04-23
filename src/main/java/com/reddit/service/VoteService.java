package com.reddit.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.reddit.dto.VoteDto;
import com.reddit.exceptions.PostNotFoundException;
import com.reddit.exceptions.SpringRedditException;
import com.reddit.model.Post;
import com.reddit.model.Vote;
import com.reddit.repository.PostRepository;
import com.reddit.repository.VoteRepository;

import lombok.AllArgsConstructor;


import static com.reddit.model.VoteType.UPVOTE;


@Service
@AllArgsConstructor
public class VoteService {

	 private final VoteRepository voteRepository;
	 private final PostRepository postRepository;
	 private final AuthService authService;
	

	 @Transactional
	    public VoteDto vote(VoteDto voteDto) {
	        Post post = postRepository.findById(voteDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
	        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
	        if (voteByPostAndUser.isPresent() &&
	                voteByPostAndUser.get().getVoteType()
	                        .equals(voteDto.getVoteType())) {
	            throw new SpringRedditException("You have already "
	                    + voteDto.getVoteType() + "'d for this post");
	        }
	        if (UPVOTE.equals(voteDto.getVoteType())) {
	            post.setVoteCount(post.getVoteCount() + 1);
	        } else {
	            post.setVoteCount(post.getVoteCount() - 1);
	        }
	        voteRepository.save(mapToVote(voteDto, post));
	        postRepository.save(post);
	        
	        return voteDto;
	    }

	    private Vote mapToVote(VoteDto voteDto, Post post) {
	        return Vote.builder()
	                .voteType(voteDto.getVoteType())
	                .post(post)
	                .user(authService.getCurrentUser())
	                .build();
	    }
}
