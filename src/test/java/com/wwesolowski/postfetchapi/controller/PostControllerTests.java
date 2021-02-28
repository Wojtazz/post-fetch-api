package com.wwesolowski.postfetchapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wwesolowski.postfetchapi.model.Post;
import com.wwesolowski.postfetchapi.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTests {

    @MockBean
    PostService postService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldSuccess_returnPosts() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postService.getAllPosts(null)).thenReturn(Collections.singletonList(post));
        mockMvc.perform(get("/posts")).andDo(print()).andExpect(status().isOk());
    }
    @Test
    public void shouldSuccess_returnSynchronizedPosts() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postService.synchronizeAllPosts()).thenReturn(Collections.singletonList(post));
        mockMvc
                .perform(get("/posts/synchronize"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    public void shouldSuccess_updatePost() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        when(postService.updatePost(anyInt(),any(),any())).thenReturn(post);
        mockMvc
                .perform(put("/posts/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"test\",\"body\":\"test\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.body").value("test"));
    }
    @Test
    public void shouldSuccess_deletePost() throws Exception {
        Post post = new Post(1, "test", "test");
        post.setId(1);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/posts/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
