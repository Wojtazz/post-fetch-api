package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao extends JpaRepository<Post, Integer> {
}
