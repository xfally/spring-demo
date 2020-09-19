package io.github.xfally.springdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB配置
 *
 * @author pax
 * @since 2020-09-11
 */
@Configuration
@EnableMongoRepositories(basePackages = "io.github.xfally.springdemo.dao.ds0.repository")
public class MongoConfig {
    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory factory) {
        return new MongoTransactionManager(factory);
    }
}
