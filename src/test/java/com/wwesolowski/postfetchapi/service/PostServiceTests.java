package com.wwesolowski.postfetchapi.service;

import com.wwesolowski.postfetchapi.dao.PostDao;
import com.wwesolowski.postfetchapi.model.Post;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PostServiceTests {

    @InjectMocks
    PostService postService;

    @Mock
    PostDao postDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSuccess_createPostAndReturnPostsTest() {
        Post post = new Post(1, "test", "test");
        when(postDao.findAll()).thenReturn(Collections.singletonList(post));
        List<Post> posts = postService.getAllPosts(null);
        Assert.assertEquals(1, posts.size());
    }

    @Test
    public void shouldSuccess_updatePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postDao.findById(1)).thenReturn(java.util.Optional.of(post));
        when(postDao.saveAndFlush(any(Post.class))).thenReturn(post);
        Post createdPost = postService.updatePost(post.getId(), "edited", "edited");
        Assert.assertEquals("edited", createdPost.getTitle());
        Assert.assertEquals("edited", createdPost.getBody());
    }

    @Test(expected = NullPointerException.class)
    public void shouldFail_updatePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postDao.findById(1)).thenReturn(java.util.Optional.of(post));
        when(postDao.saveAndFlush(any(Post.class))).thenReturn(post);
        Post createdPost = postService.updatePost(2, "edited", "edited");
        Assert.assertEquals("edited", createdPost.getTitle());
        Assert.assertEquals("edited", createdPost.getBody());
    }

    @Test
    public void shouldSuccess_deletePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postDao.findById(1)).thenReturn(java.util.Optional.of(post));
        postService.deletePost(post.getId());
        verify(postDao, times(1)).delete(post);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFail_deletePostAndReturnPostTest() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postDao.findById(1)).thenReturn(java.util.Optional.of(post));
        postService.deletePost(2);
        verify(postDao, times(1)).delete(post);
    }
}
