package com.wwesolowski.postfetchapi.controller;

import com.wwesolowski.postfetchapi.dto.GetAllPostsDto;
import com.wwesolowski.postfetchapi.dto.PostDto;
import com.wwesolowski.postfetchapi.dto.UpdatePostDto;
import com.wwesolowski.postfetchapi.exception.NotFoundException;
import com.wwesolowski.postfetchapi.model.Post;
import com.wwesolowski.postfetchapi.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity getAllPosts(@RequestBody(required = false) GetAllPostsDto getAllPostsDto) {
        List<PostDto> allPosts;
        try {
            if (getAllPostsDto == null) {
                allPosts = postService.
                        getAllPosts(null)
                        .stream()
                        .map(post -> modelMapper.map(post, PostDto.class))
                        .collect(Collectors.toList());
            } else {
                allPosts = postService
                        .getAllPosts(getAllPostsDto.getTitle())
                        .stream()
                        .map(post -> modelMapper.map(post, PostDto.class))
                        .collect(Collectors.toList());
            }
            return ResponseEntity.status(HttpStatus.OK).body(allPosts);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/synchronize")
    public ResponseEntity synchronizePosts() {
        try {
            List<PostDto> synchronizedPosts = postService.synchronizeAllPosts()
                    .stream()
                    .map(post -> modelMapper.map(post, PostDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(synchronizedPosts);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable Integer id, @RequestBody UpdatePostDto updatePostDto) {
        try {
            Post post = postService.updatePost(id, updatePostDto.getTitle(), updatePostDto.getBody());
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(post, PostDto.class));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Integer id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}