package com.example.demo.config;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pax
 */
@Configuration
public class RabbitMQConfig {
    @Getter
    @Value("${rabbitmq.queue.msg}")
    private String queueName;

    @Bean
    public Queue createQueue() {
        // 参数：消息队列名，是否持久化消息
        return new Queue(queueName, true);
    }
}
