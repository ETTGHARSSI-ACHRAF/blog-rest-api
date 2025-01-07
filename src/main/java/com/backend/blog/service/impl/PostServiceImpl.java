package com.backend.blog.service.impl;

import com.backend.blog.entity.Post;
import com.backend.blog.exeception.ResourceNotFoundException;
import com.backend.blog.payload.PostDto;
import com.backend.blog.payload.PostResponse;
import com.backend.blog.repository.PostRepository;
import com.backend.blog.service.PostSevice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostSevice {

    private ModelMapper mapper;

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper){
        this.mapper = mapper;
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //convert DTO to entity
        Post post = mapToEntity(postDto);
        //save
       Post newPost =  postRepository.save(post);

       // convert entity to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

    @Override
    public PostResponse findAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

      Page<Post> posts = postRepository.findAll(pageable);

      //get content for page object
        List<Post> listeOfPosts = posts.getContent();
     List<PostDto> content =  listeOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPage(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto findPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
        // PostDto postDto = new PostDto();
        // postDto.setId(post.getId());
        // postDto.setTitle(post.getTitle());
        // postDto.setDescription(post.getDescription());
        // postDto.setContent(post.getContent());
        return postDto;
    }

    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
       // Post post = new Post();
       // post.setTitle(postDto.getTitle());
       // post.setDescription(postDto.getDescription());
       // post.setContent(postDto.getContent());
        return post;
    }
}
