package com.reddit.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.reddit.dto.ApiResponse;
import com.reddit.dto.PostRequest;
import com.reddit.dto.PostResponse;
import com.reddit.dto.SubredditDto;
import com.reddit.exceptions.PostNotFoundException;
import com.reddit.exceptions.SubredditNotFoundException;
import com.reddit.mapper.PostMapper;
import com.reddit.model.Post;
import com.reddit.model.Subreddit;
import com.reddit.model.User;
import com.reddit.model.Vote;
import com.reddit.model.VoteType;
import com.reddit.repository.CommentRepository;
import com.reddit.repository.PostRepository;
import com.reddit.repository.SubredditRepository;
import com.reddit.repository.UserRepository;
import com.reddit.repository.VoteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import static com.reddit.model.VoteType.DOWNVOTE;
import static com.reddit.model.VoteType.UPVOTE;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final CommentRepository commentRepository;
//	@Autowired
//	private PostMapper postMapper;
	private final UserRepository userRepository;
	private final VoteRepository voteRepository;
	
//	public Post save(PostRequest postRequest) {
	public PostRequest save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName());
		log.debug("Subreddit ", subreddit);
		
		User currentUser = authService.getCurrentUser();
		log.debug("Current User ", currentUser);
	
		Post save =  postRepository.save(map(postRequest, subreddit, currentUser));
		postRequest.setPostId(save.getPostId());
		return postRequest;
	}
	

	private Post map(PostRequest postRequest, Subreddit subreddit, User currentUser) {
		return Post.builder()
//				.postId(postRequest.getPostId())
				.postName(postRequest.getPostName())
				.description(postRequest.getDescription())
				.url(postRequest.getUrl())
				.user(currentUser)
				.subreddit(subreddit)
				.createdDate(Instant.now())
				.voteCount(0)
				.build();
	}


	public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToDto)
//                .map(postMapper::mapToDto)
                .toList();
    }
	
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return mapToDto(post);
    }
	

    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(this::mapToDto).toList();
    }
	

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user==null) {
	    	   new UsernameNotFoundException(("User Not Found "+ username));
	       }
//                .orElseThrow(() -> new UsernameNotFoundException(username));
        
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    
    
//	GET ALL ALTERNATIVE TO MAPPER
	private PostResponse mapToDto(Post post) {
		return PostResponse.builder()
				.id(post.getPostId())
				.subredditName(post.getSubreddit().getName())
				.postName(post.getPostName())
				.url(post.getUrl())
				.description(post.getDescription())
				.commentCount(commentCount(post))
				.userName(post.getUser().getUsername())
				.voteCount(post.getVoteCount())
				.upVote(isPostUpVoted(post))
				.downVote(isPostDownVoted(post))
//				.duration(getDuration(post))
				.build();
	}
	
	private Integer commentCount(Post post) { return commentRepository.findByPost(post).size();}
//	private String getDuration(Post post) { return TimeAgo.using(post.getCreatedDate().toEpochMilli());}
	
	
	boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }
	
	private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                    authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

	@Transactional
	public PostRequest update(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName());
		log.debug("Subreddit ", subreddit);
		
		User currentUser = authService.getCurrentUser();
		log.debug("Current User ", currentUser);
	
		Post existing = postRepository.findById(postRequest.getPostId()).get();
			
		existing.setCreatedDate(Instant.now());
		existing.setPostName(postRequest.getPostName());
		existing.setDescription(postRequest.getDescription());
		existing.setUrl(postRequest.getUrl());
		existing.setUser(currentUser);
		existing.setSubreddit(subreddit);
		Post save = postRepository.save(existing);
//		Post save = postRepository.save(existing.builder()
//				.createdDate(Instant.now())
//				.description(postRequest.getDescription())
//				.url(postRequest.getUrl())
//				.user(currentUser)
//				.subreddit(subreddit)
//				.build());	
		postRequest.setPostId(save.getPostId());
		return postRequest;
	}


	public List<PostResponse> getAllPostsByPaginate1(Optional<Integer> page, Optional<String> sortBy) {
		// TODO Auto-generated method stub
		return postRepository.findAll(PageRequest.of(
				page.orElse(0), 
				5, 
				Sort.Direction.DESC, sortBy.orElse("postId"))
				).stream()
                .map(this::mapToDto)
//              .map(postMapper::mapToDto)
              .toList();
	}
	//issue for data serialize
	public Page<Post> getAllPostsByPaginate(Integer pageNumber,Integer pageSize) {
		// TODO Auto-generated method stub
		return postRepository.findAll(PageRequest.of(pageNumber, pageSize));
	}
}
