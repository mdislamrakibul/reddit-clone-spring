package com.reddit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.dto.ApiResponse;
import com.reddit.dto.CommentsDto;
import com.reddit.service.CommentsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentsController {

	private final CommentsService commentService;
	
	@PostMapping
	public ResponseEntity<ApiResponse> createComment(@RequestBody CommentsDto commentsDto) {
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Comments Created", commentService.save(commentsDto)));
//		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentsDto));
	}
	
	@GetMapping("/by-post/{postId}")
	public ResponseEntity<ApiResponse> getALlCommentsForPost (@PathVariable Long postId) {
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponse(true, "Comments Found By Post Id", commentService.getALlCommentsForPost(postId)));
//		return ResponseEntity.status(HttpStatus.OK).body(commentService.getALlCommentsForPost(postId));
	}
	
	@GetMapping("/by-user/{username}")
	public ResponseEntity<ApiResponse> getCommentsByUser (@PathVariable String username) {
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponse(true, "Comments Found By User", commentService.getCommentsByUser(username)));
//		return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByUser(username));
	}
}
