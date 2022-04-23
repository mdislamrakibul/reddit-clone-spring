package com.reddit.dto;

import static javax.persistence.FetchType.LAZY;

import java.time.Instant;
import java.util.List;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import com.reddit.model.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDto {

	private Long id;
    private String name;
    private String description;
    private Integer postCount;  
    //new add
	private String username;
    
	
}
