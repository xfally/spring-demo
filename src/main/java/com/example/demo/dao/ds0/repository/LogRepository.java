package com.example.demo.dao.ds0.repository;

import com.example.demo.dao.ds0.entity.LogDO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

/**
 * Repository 接口
 *
 * @author pax
 * @since 2020-08-31
 **/
public interface LogRepository extends MongoRepository<LogDO, String> {
    Set<LogDO> findByLevel(String level);
}
