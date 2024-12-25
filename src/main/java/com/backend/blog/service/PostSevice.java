package com.backend.blog.service;

import com.backend.blog.payload.PostDto;
import com.backend.blog.payload.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostSevice {
    PostDto createPost(PostDto postDto);
    PostResponse findAllPost(int pageNo, int pageSize);

    PostDto findPostById(long id);

    PostDto updatePost(long id, PostDto postDto);

    void deletePost(Long id);
}
