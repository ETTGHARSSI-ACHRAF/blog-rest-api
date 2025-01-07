package com.backend.blog.service;

import com.backend.blog.entity.Comment;
import com.backend.blog.payload.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);
    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentsByIdAndPostId(long postId, long id);
}
