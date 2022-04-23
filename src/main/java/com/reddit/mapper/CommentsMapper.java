package com.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.dto.CommentsDto;
import com.reddit.model.Comment;
import com.reddit.model.Post;
import com.reddit.model.User;

@Mapper(componentModel = "spring")
public interface CommentsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}