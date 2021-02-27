package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post, Integer> {

    List<Post> findByTitleContainingIgnoreCase(String title);
    List<Post> findAllByOrderByIdAsc();
}
