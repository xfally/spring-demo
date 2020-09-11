package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * MongoDB配置
 *
 * @author pax
 * @since 2020-09-11
 */
// MongoDB只有启用集群，才能支持事务特性。
//@Configuration
public class MongoConfig {
    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory factory) {
        return new MongoTransactionManager(factory);
    }
}
