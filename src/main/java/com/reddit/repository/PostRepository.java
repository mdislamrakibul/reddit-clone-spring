package com.reddit.repository;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import com.reddit.model.Post;
import com.reddit.model.Subreddit;
import com.reddit.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
