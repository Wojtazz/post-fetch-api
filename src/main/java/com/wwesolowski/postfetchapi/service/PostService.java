package com.wwesolowski.postfetchapi.service;

import com.wwesolowski.postfetchapi.dao.PostDao;
import com.wwesolowski.postfetchapi.exception.NotFoundException;
import com.wwesolowski.postfetchapi.model.Post;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    public List<Post> getAllPosts(String title) {
        if (title != null) {
            return postDao.findByTitleContainingIgnoreCase(title);
        }
        return postDao.findAll();
    }

    public Post updatePost(Integer id, String title, String body) throws Exception {
        Post post = postDao.findById(id).orElse(null);
        if(post == null) {
            throw new NotFoundException("Post with specific id doesn't exists");
        }
        if(title != null) {
            post.setTitle(title);
        }
        if(body != null) {
            post.setBody(body);
        }
        return postDao.saveAndFlush(post);
    }

    public void deletePost(Integer id) throws Exception {
        Post post = postDao.findById(id).orElse(null);
        if(post == null) {
            throw new NotFoundException("Post with specific id doesn't exist");
        }
        postDao.delete(post);
    }
}
