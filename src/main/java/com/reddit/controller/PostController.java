package com.reddit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.dto.ApiResponse;
import com.reddit.dto.PostRequest;
import com.reddit.dto.PostResponse;
import com.reddit.exceptions.SubredditNotFoundException;
import com.reddit.model.Post;
import com.reddit.model.Subreddit;
import com.reddit.model.User;
import com.reddit.repository.PostRepository;
import com.reddit.repository.SubredditRepository;
import com.reddit.service.AuthService;
import com.reddit.service.PostService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.ResponseEntity.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PostController {
	private final PostService postService;
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final PostRepository postRepository;
	
	
	@PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostRequest postRequest) {
		
		return status(HttpStatus.OK).body(new ApiResponse(true, "Post Created", postService.save(postRequest))) ;
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequest));
        
    }
	
	@PutMapping
    public ResponseEntity<ApiResponse> updatePost(@RequestBody PostRequest postRequest) {
		Post existing = postRepository.findById(postRequest.getPostId()).get();
		if(existing == null) {
			return status(HttpStatus.OK).body(new ApiResponse(true, "Post Not Found", "")) ;
		}
		return status(HttpStatus.OK).body(new ApiResponse(true, "Post Updated Successfully", postService.update(postRequest))) ;
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequest));
        
    }
	
	
	@GetMapping
    public ResponseEntity<ApiResponse> getAllPosts() {
        return status(HttpStatus.OK).body(new ApiResponse(true, "Post Found", postService.getAllPosts())) ;
    }
	
	@GetMapping("page1")
    public ResponseEntity<ApiResponse> getAllPostsByPaginate1(
    		@RequestParam Optional<Integer> page, 
    		@RequestParam Optional<String> sortBy
    		) {
        return status(HttpStatus.OK).body(new ApiResponse(true, "Post Found", postService.getAllPostsByPaginate1(page, sortBy))) ;
    }
	
	@GetMapping("page")
    public Page<Post> getAllPostsByPaginate(
    		@RequestParam Integer pageSize,
    		@RequestParam Integer pageNumber
    		) {
        return postService.getAllPostsByPaginate(pageNumber, pageSize);
    }
	
	
	@GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long id) {
		return status(HttpStatus.OK).body(new ApiResponse(true, "Post Found", postService.getPost(id))) ;
//        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<ApiResponse> getPostsBySubreddit(@PathVariable Long id) {
    	return status(HttpStatus.OK).body(new ApiResponse(true, "Post Found", postService.getPostsBySubreddit(id))) ;
//        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<ApiResponse> getPostsByUsername(@PathVariable String username) {
    	return status(HttpStatus.OK).body(new ApiResponse(true, "Post Found", postService.getPostsByUsername(username))) ;
//        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }
}
