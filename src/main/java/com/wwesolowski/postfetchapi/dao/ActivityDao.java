package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityDao extends JpaRepository<Activity, Integer> {
}
