package com.rulin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 检测实时在线人数
 *
 */

@Component
@ServerEndpoint("/websocket")
public class OnlineSocketServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(OnlineSocketServiceImpl.class);

    /**
     * 初始在线人数
     */
    private static int online_num = 0;
    /**
     * 线程安全的socket集合
     */
    private static CopyOnWriteArraySet<OnlineSocketServiceImpl> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 会话
     */
    private Session session;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        logger.info("有链接加入，当前人数为:{}",getOnlineNum());
        synchronized(session) {
            this.session.getBasicRemote().sendText(getOnlineNum());
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        logger.info("有链接关闭,当前人数为:{}",getOnlineNum());
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        logger.info("来自客户端的消息:{}",message);
        for (OnlineSocketServiceImpl websocketService : webSocketSet) {
            synchronized(websocketService.session) {
                websocketService.session.getAsyncRemote().sendText(getOnlineNum());
            }
        }
    }

    public synchronized String getOnlineNum(){
        return OnlineSocketServiceImpl.online_num+"";
    }

    public synchronized int subOnlineCount(){
        return OnlineSocketServiceImpl.online_num--;
    }

    public synchronized int addOnlineCount(){
        return OnlineSocketServiceImpl.online_num++;
    }
}
