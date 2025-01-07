package com.backend.blog.service.impl;

import com.backend.blog.entity.Comment;
import com.backend.blog.entity.Post;
import com.backend.blog.exeception.BlogApiException;
import com.backend.blog.exeception.ResourceNotFoundException;
import com.backend.blog.payload.CommentDto;
import com.backend.blog.repository.CommentRepository;
import com.backend.blog.repository.PostRepository;
import com.backend.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        //create new comment entity with value of dto
        Comment comment = mapToEntity(commentDto);

        //get post with id post
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post", "id", postId));

        //set variable post of the comment with post return
        comment.setPost(post);

        //save comment
        Comment commentEntity =  commentRepository.save(comment);

        return mapToDto(commentEntity);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentsByIdAndPostId(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        //Comment comment = commentRepository.findCommentByIdAndPostId(postId, id)
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment", "id", id));
        if(!Long.valueOf(comment.getPost().getId()).equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment", "id", id));
        if(!Long.valueOf(comment.getPost().getId()).equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment commentUpdated = commentRepository.save(comment);
        return mapToDto(commentUpdated);
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment", "id", id));
        if(!Long.valueOf(comment.getPost().getId()).equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        commentRepository.deleteById(id);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
