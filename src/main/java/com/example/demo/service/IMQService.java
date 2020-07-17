package com.example.demo.service;

/**
 * 消息队列服务
 *
 * @author pax
 */
public interface IMQService {
    /**
     * 发送消息
     *
     * @param msg 消息
     */
    void sendMsg(String msg);
}
