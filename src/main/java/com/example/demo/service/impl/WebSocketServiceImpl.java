package com.example.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author pax
 */
@Service
@ServerEndpoint("/ws")
@Slf4j
public class WebSocketServiceImpl {
    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    private Session session;
    private String address;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        address = getRemoteAddress(session);
        sessions.add(session);
        addOnlineCount();
        log.info("有新连接（ID={}, IP={}）。当前客户数={}", session.getId(), address, getOnlineCount());
        sendMessage("欢迎加入！");
    }

    @OnClose
    public void onClose() {
        sessions.remove(session);
        subOnlineCount();
        log.info("有连接断开（ID={}, IP={}）。当前客户数={}", session.getId(), address, getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message) {
        log.debug("来自客户端(ID={}, IP={})的消息：【{}】", session.getId(), address, message);
    }

    @OnError
    public void onError(Throwable error) {
        log.warn("客户端(ID={}, IP={})发生错误：{}", session.getId(), address, error.getMessage());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void pubMessage(String message) throws IOException {
        for (Session session0 : sessions) {
            session0.getBasicRemote().sendText(message);
        }
    }

    private String getRemoteAddress(Session session) {
        InetSocketAddress inetSocketAddress = getInetSocketAddress(session);
        if (inetSocketAddress == null) {
            return null;
        }
        return inetSocketAddress.getAddress().getHostAddress();
    }

    private InetSocketAddress getInetSocketAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();

        //在Tomcat 8.0.x版本有效
        //InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
        //在Tomcat 8.5以上版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        return addr;
    }

    private Object getFieldInstance(Object obj, String fieldPath) {
        String[] fields = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }

        return obj;
    }

    private Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
                // ignore
            }
        }

        return null;
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        onlineCount--;
    }
}
