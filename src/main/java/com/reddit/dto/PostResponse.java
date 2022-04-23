package com.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
	private Long id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subredditName;
    
    //new field
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    
    //vote purpose
    private boolean upVote;
    private boolean downVote;
}
