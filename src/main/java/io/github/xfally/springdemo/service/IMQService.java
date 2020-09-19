package io.github.xfally.springdemo.service;

/**
 * 消息队列服务
 *
 * @author pax
 * @since 2020-03-19
 */
public interface IMQService {
    /**
     * 发送消息
     *
     * @param msg 消息
     */
    void sendMsg(String msg);
}
