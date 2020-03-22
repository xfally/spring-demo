package com.example.demo.service.impl;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.service.IMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author pax
 */
@Service
@Slf4j
public class MQServiceImpl implements IMQService, RabbitTemplate.ConfirmCallback {
    private String queueName;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void initQueue() {
        queueName = rabbitMQConfig.getQueueName();
    }

    @Override
    public void sendMsg(String msg) {
        log.debug("发送消息：【{}】", msg);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend(queueName, msg);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
            log.debug("消息成功消费。");
        } else {
            log.warn("消息消费失败：" + s);
        }
    }

    // 这里为了测试方便，将消息接收与消息发送放到同一个类里。
    @RabbitListener(queues = {"${rabbitmq.queue.msg}"})
    public void receiveMsg(String msg) {
        log.debug("收到消息：【{}】", msg);
    }
}
