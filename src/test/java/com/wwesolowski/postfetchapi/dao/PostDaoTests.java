package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Post;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.List;

@DataJpaTest
public class PostDaoTests {

    @Autowired
    PostDao postDao;

    @Test
    public void shouldSuccess_testSavePost() {
        Post post = new Post(1, 1, "test", "test");
        postDao.save(post);
    }
    @Test
    public void shouldFail_testSavePost() {
        Post post = new Post(null, 1, "test", "test");
        Assert.assertThrows(JpaSystemException.class, () -> postDao.save(post));
    }
    @Test
    public void shouldSuccess_testFindAllPosts() {
        Post post = new Post(1, 1, "test", "test");
        postDao.save(post);
        List<Post> allPosts = (List<Post>) postDao.findAll();
        Assert.assertEquals(1, allPosts.size());
    }
    @Test
    public void shouldFail_testFindAllPosts() {
        List<Post> allPosts = (List<Post>) postDao.findAll();
        Assert.assertNotEquals(1, allPosts.size());
    }
    @Test
    public void shouldSuccess_testEditPost() {
        Post post = new Post(1, 1, "test", "test");
        Post editedPost = postDao.save(post);
        editedPost.setTitle("edited");
        postDao.save(editedPost);
        Assert.assertNotEquals(post.getTitle(), editedPost.getTitle());
    }
    @Test
    public void shouldFail_testEditPost() {
        Post post = new Post(1, 1, "test", "test");
        Post editedPost = postDao.save(post);
        post.setTitle("testnew");
        Assert.assertEquals("test", editedPost.getTitle());
    }
    @Test
    public void shouldSuccess_testDeletePost() {
        Post post = new Post(1, 1, "test", "test");
        postDao.save(post);
        postDao.delete(post);
        Assert.assertNull(postDao.findById(1).orElse(null));
    }
    @Test
    public void shouldFail_testDeletePost() {
        Post post = null;
        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> postDao.delete(post));
    }
}
