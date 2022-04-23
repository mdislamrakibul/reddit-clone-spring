package com.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.model.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
	 Subreddit findByName(String subredditName);
}
