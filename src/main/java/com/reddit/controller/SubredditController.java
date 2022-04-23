package com.reddit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.dto.ApiResponse;
import com.reddit.dto.SubredditDto;
import com.reddit.model.Subreddit;
import com.reddit.repository.SubredditRepository;
import com.reddit.service.SubredditService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/subreddit")
@CrossOrigin(origins = "*")
public class SubredditController {

	@Autowired
	private SubredditService subredditService;
	@Autowired
	private SubredditRepository subredditRepository;
	
	@PostMapping
	public ResponseEntity<ApiResponse> createSubreddit(@RequestBody SubredditDto subredditDto) {
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Subreddit Created", subredditService.Save(subredditDto)));
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse> updateSubreddit(@RequestBody SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.findById(subredditDto.getId()).get();
		if(subreddit == null) {
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, "Subreddit Not Found", ""));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Subreddit Updated", subredditService.update(subredditDto)));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse> getAllSubreddits() {
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Subreddit Found", subredditService.getAll()));
//		return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getAllSubredditsById(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Subreddit Found", subredditService.getSubredditById(id)));
//		return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubredditById(id));
	}
}
