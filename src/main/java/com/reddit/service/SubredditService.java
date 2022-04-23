package com.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import com.reddit.dto.SubredditDto;
import com.reddit.exceptions.SpringRedditException;
import com.reddit.mapper.SubredditMapper;
import com.reddit.model.Subreddit;
import com.reddit.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

	@Autowired
	private SubredditRepository subredditRepository;
	private final AuthService authService;
//	@Autowired
//	private SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto Save (SubredditDto subredditDto) {
		 Subreddit save = subredditRepository.save(mapToSubreddit(subredditDto));
//		Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(save.getId());
		return subredditDto;
	}
	
	// SAVE ALTERNATIVE FOR MAPPER
	private Subreddit mapToSubreddit(SubredditDto subredditDto) {
		return Subreddit.builder().name("/r/" + subredditDto.getName())
				.description(subredditDto.getDescription())
				.user(authService.getCurrentUser())
				.createdDate(Instant.now())
				.build();
		
	}
	
	@Transactional
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll()
		.stream()
//		.map(subredditMapper::mapSubredditToDto).toList();
		.map(this::mapToDto).toList();
	}
	
	
	@Transactional
	public SubredditDto getSubredditById(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(()-> new SpringRedditException("Subreddit Not Found"));
		
//		return subredditMapper.mapSubredditToDto(subreddit);
		return SubredditDto.builder().name(subreddit.getName())
				.id(subreddit.getId())
				.description(subreddit.getDescription())
				.postCount(subreddit.getPosts().size())
				.username(subreddit.getUser().getUsername())
				.build();
	}
	
	//	GET ALL ALTERNATIVE TO MAPPER
	private SubredditDto mapToDto(Subreddit subreddit) {
		return SubredditDto.builder().name(subreddit.getName())
				.id(subreddit.getId())
				.description(subreddit.getDescription())
				.postCount(subreddit.getPosts().size())
				.username(subreddit.getUser().getUsername())
				.build();
	}

	public SubredditDto update(SubredditDto subredditDto) {
		
		Subreddit existing = subredditRepository.findById(subredditDto.getId()).get();
		
		existing.setName(subredditDto.getName());
		existing.setDescription(subredditDto.getDescription());
		Subreddit save = subredditRepository.save(existing);
		// TODO Auto-generated method stub
		return subredditDto;
	}

	

	
}
