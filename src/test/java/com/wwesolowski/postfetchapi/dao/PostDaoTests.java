package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Post;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ConstraintViolationException;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostDaoTests {

    @Autowired
    PostDao postDao;

    @BeforeEach
    public void setup() {
        Post post1 = new Post(1, "test1", "test1");
        Post post2 = new Post(2, "test2", "test2");
        Post post3 = new Post(3, "test3", "test3");
        postDao.saveAndFlush(post1);
        postDao.saveAndFlush(post2);
        postDao.saveAndFlush(post3);
    }

    @Test
    public void shouldSuccess_testSavePost() {
        Post post = new Post(4, "test4", "test4");
        postDao.saveAndFlush(post);
    }
    @Test
    public void shouldFail_testSavePost() {
        Post post = new Post(4, null, "test4");
        Assert.assertThrows(ConstraintViolationException.class, () -> postDao.saveAndFlush(post));
    }
    @Test
    public void shouldSuccess_testFindAllPosts() {
        Post post = new Post(4, "test4", "test4");
        postDao.saveAndFlush(post);
        List<Post> allPosts = postDao.findAll();
        Assert.assertEquals(4, allPosts.size());
    }
    @Test
    public void shouldFail_testFindAllPosts() {
        List<Post> allPosts = postDao.findAll();
        Assert.assertNotEquals(4, allPosts.size());
    }
    @Test
    public void shouldSuccess_testEditPost() {
        Post post = postDao.findById(1).orElse(null);
        post.setTitle("edited");
        postDao.saveAndFlush(post);
        Assert.assertNotEquals(post.getTitle(), postDao.findById(1).orElse(null));
    }
    @Test
    public void shouldFail_testEditPost() {
        Assert.assertThrows(NullPointerException.class,() -> postDao.findById(4).orElse(null).setTitle("test4"));
    }
    @Test
    public void shouldSuccess_testDeletePost() {
        Post post = postDao.findById(1).orElse(null);
        postDao.delete(post);
        Assert.assertNull(postDao.findById(1).orElse(null));
    }
    @Test
    public void shouldFail_testDeletePost() {
        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> postDao.delete(postDao.findById(4).orElse(null)));
    }
}
