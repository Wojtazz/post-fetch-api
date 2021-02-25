package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostDao extends CrudRepository<Post, Integer> {
}
