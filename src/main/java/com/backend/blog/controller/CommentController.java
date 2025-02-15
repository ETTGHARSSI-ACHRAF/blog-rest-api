package com.backend.blog.controller;

import com.backend.blog.payload.CommentDto;
import com.backend.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentDto> saveComment(@PathVariable(value = "postId") long postId, @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}/comments")
    public List<CommentDto> getAllCommentOfPost(@PathVariable long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/post/{postId}/comment/{id}")
    public CommentDto getCommentOfPostById(@PathVariable long postId, @PathVariable long id){
        return commentService.getCommentsByIdAndPostId(postId, id);
    }

    @PostMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long postId, @PathVariable long id, @RequestBody CommentDto commentDto){
        CommentDto commentDtoResponse = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable long postId, @PathVariable long id){
        commentService.deleteComment(postId, id);
        return new ResponseEntity<>("Comment deleted successfully",HttpStatus.OK);
    }

}
