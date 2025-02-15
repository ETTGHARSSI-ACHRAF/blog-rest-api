package com.backend.blog.repository;

import com.backend.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(long postId);
    Comment findCommentByIdAndPostId(long postId, long id);
}
