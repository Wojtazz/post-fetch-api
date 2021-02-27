package com.wwesolowski.postfetchapi.service;

import com.wwesolowski.postfetchapi.dao.ActivityDao;
import com.wwesolowski.postfetchapi.dao.PostDao;
import com.wwesolowski.postfetchapi.exception.BodyInvalidException;
import com.wwesolowski.postfetchapi.exception.NotFoundException;
import com.wwesolowski.postfetchapi.model.Activity;
import com.wwesolowski.postfetchapi.model.ModifyType;
import com.wwesolowski.postfetchapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private ActivityDao activityDao;

    public List<Post> synchronizeAllPosts() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Post>> postsResponse =
                restTemplate.exchange("https://jsonplaceholder.typicode.com/posts",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
                        });
        List<Integer> postsActivitesIds = activityDao.findAll()
                .stream()
                .map(activity -> activity.getPostId())
                .collect(Collectors.toList());
        List<Post> synchronizedPosts = postsResponse
                .getBody()
                .stream()
                .filter(post -> !postsActivitesIds.contains(post.getId()))
                .collect(Collectors.toList());
        postDao.saveAll(synchronizedPosts);
        return synchronizedPosts;
    }

    public List<Post> getAllPosts(String title) {
        if (title != null) {
            return postDao.findByTitleContainingIgnoreCase(title);
        }
        return postDao.findAllByOrderByIdAsc();
    }

    public Post updatePost(Integer id, String title, String body) throws Exception {
        Post post = postDao.findById(id).orElseThrow(() -> new NotFoundException("Post with specific id doesn't exists"));
        if(title == null && body == null) {
            throw new BodyInvalidException("RequestBody cannot be empty and need to have title or/and body value");
        }
        Activity activity = activityDao.findByPostId(post.getId()).orElse(new Activity());
        activity.setPostId(post.getId());
        activity.setModifyType(ModifyType.EDIT);
        activity.setModifyDate(new Date());
        activityDao.save(activity);
        if(title != null) {
            post.setTitle(title);
        }
        if(body != null) {
            post.setBody(body);
        }
        return postDao.saveAndFlush(post);
    }

    public void deletePost(Integer id) throws Exception {
        Post post = postDao.findById(id).orElseThrow(() -> new NotFoundException("Post with specific id doesn't exist"));
        Activity activity = activityDao.findByPostId(post.getId()).orElse(new Activity());
        activity.setPostId(post.getId());
        activity.setModifyType(ModifyType.DELETE);
        activity.setModifyDate(new Date());
        activityDao.saveAndFlush(activity);
        postDao.delete(post);
    }
}
