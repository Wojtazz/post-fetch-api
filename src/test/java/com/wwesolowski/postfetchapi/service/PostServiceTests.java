package com.wwesolowski.postfetchapi.service;

import com.wwesolowski.postfetchapi.dao.ActivityDao;
import com.wwesolowski.postfetchapi.dao.PostDao;
import com.wwesolowski.postfetchapi.model.Activity;
import com.wwesolowski.postfetchapi.model.ModifyType;
import com.wwesolowski.postfetchapi.model.Post;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PostServiceTests {

    @InjectMocks
    PostService postService;

    @Mock
    PostDao postDao;

    @Mock
    ActivityDao activityDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSuccess_returnSynchronizedPostsTest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Post>> postsResponse =
                restTemplate.exchange("https://jsonplaceholder.typicode.com/posts",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
                        });
        Activity activity = new Activity(1, ModifyType.EDIT, new Date());
        when(activityDao.findAll()).thenReturn(Collections.singletonList(activity));
        List<Post> posts = postService.synchronizeAllPosts();
        Assert.assertEquals(postsResponse.getBody().size() - 1, posts.size());
    }
    @Test
    public void shouldSuccess_createPostAndReturnPostsTest() {
        Post post = new Post(1, "test", "test");
        when(postDao.findAllByOrderByIdAsc()).thenReturn(Collections.singletonList(post));
        List<Post> posts = postService.getAllPosts(null);
        Assert.assertEquals(1, posts.size());
    }
    @Test
    public void shouldSuccess_updatePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        Activity activity = new Activity(1, ModifyType.EDIT, new Date());
        when(postDao.findById(anyInt())).thenReturn(java.util.Optional.of(post));
        when(activityDao.findByPostId(anyInt())).thenReturn(java.util.Optional.of(activity));
        when(postDao.saveAndFlush(any(Post.class))).thenReturn(post);
        Post createdPost = postService.updatePost(post.getId(), "edited", "edited");
        Assert.assertEquals("edited", createdPost.getTitle());
        Assert.assertEquals("edited", createdPost.getBody());
    }

    @Test(expected = NullPointerException.class)
    public void shouldFail_updatePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        when(postDao.findById(anyInt())).thenReturn(java.util.Optional.of(post));
        when(postDao.saveAndFlush(any(Post.class))).thenReturn(post);
        Post createdPost = postService.updatePost(2, "edited", "edited");
        Assert.assertEquals("edited", createdPost.getTitle());
        Assert.assertEquals("edited", createdPost.getBody());
    }

    @Test
    public void shouldSuccess_deletePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        Activity activity = new Activity(1, ModifyType.EDIT, new Date());
        when(postDao.findById(anyInt())).thenReturn(java.util.Optional.of(post));
        when(activityDao.findByPostId(anyInt())).thenReturn(java.util.Optional.of(activity));
        postService.deletePost(post.getId());
        verify(postDao, times(1)).delete(post);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFail_deletePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        when(postDao.findById(anyInt())).thenReturn(java.util.Optional.of(post));
        postService.deletePost(2);
        verify(postDao, times(1)).delete(post);
    }
}
