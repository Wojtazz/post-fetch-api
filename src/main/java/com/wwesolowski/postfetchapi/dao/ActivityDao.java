package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActivityDao extends JpaRepository<Activity, Integer> {
    Optional<Activity> findByPostId(Integer postId);
}
