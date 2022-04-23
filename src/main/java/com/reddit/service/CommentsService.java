package com.reddit.service;

import java.time.Instant;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reddit.dto.CommentsDto;
import com.reddit.exceptions.PostNotFoundException;
import com.reddit.model.Comment;
import com.reddit.model.NotificationEmail;
import com.reddit.model.Post;
import com.reddit.model.User;
import com.reddit.repository.CommentRepository;
import com.reddit.repository.PostRepository;
import com.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentsService {

	//TODO: Construct POST URL
    private static final String POST_URL = "";
    
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public CommentsDto save (CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(
				() -> new PostNotFoundException("Post Not Found"));
		
		User currentUser = authService.getCurrentUser();
		
		Comment save =  commentRepository.save(map(commentsDto, post, currentUser));
		commentsDto.setId(save.getId());
		
		String message = mailContentBuilder.build( post.getUser().getUsername()+" commented your post "+ POST_URL );
		sendCommentNotification(message, post.getUser());
		
		return commentsDto;
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
	}

	private Comment map(CommentsDto commentsDto, Post post, User currentUser) {

		return Comment.builder()
				.text(commentsDto.getText())
				.user(currentUser)
				.post(post)
				.createdDate(Instant.now())
				.build();
	}

	public List<CommentsDto> getALlCommentsForPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new PostNotFoundException("Post Not Found"));
		
		return commentRepository.findByPost(post)
				.stream()
				.map(this::mapToDto)
				.toList(); 
	}
	
	
	public List<CommentsDto> getCommentsByUser(String username) {
		User user = userRepository.findByUsername(username);
		if(user==null) {
	    	   new UsernameNotFoundException(("User Not Found "+ username));
	       }
//				.orElseThrow(() -> new UsernameNotFoundException(username));
		
		return commentRepository.findAllByUser(user)
				.stream()
				.map(this::mapToDto)
				.toList();
	}
	
	private CommentsDto mapToDto(Comment comment) {
		return CommentsDto.builder()
				.id(comment.getId())
				.postId(comment.getPost().getPostId())
				.userName(comment.getUser().getUsername())
				.text(comment.getText())
				.createdDate(comment.getCreatedDate())
				.build();
		
	}
}
