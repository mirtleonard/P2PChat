package com.tora.service;

import com.tora.configuration.WebSocketContainers;
import com.tora.model.GroupChat;
import com.tora.websocket.WebSocketEndpoint;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Map;

@Component
public class WebSocketService implements IService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    private final Map<String, Session> sessions = WebSocketContainers.sessions;

    @Override
    public void connectToGroupChat(String host, String chatName) throws Exception {

    }

    public void connectToChat(String host, String port) {
        WebSocketEndpoint client = new WebSocketEndpoint();
        try {
            client.connect(host, port);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String[] getConnections() {
        return WebSocketContainers.sessions.keySet().toArray(new String[0]);
    }

    @Override
    public void listenPort(int port) throws Exception {
    }

    @Override
    public void terminate() throws Exception {
    }

    @Override
    public void closeConnection(String host) throws Exception {
        Session session;
        if ((session = sessions.remove(host)) == null) {
            throw new Exception("Connection " + host + " doesn't exist: " + host);
        }
        session.close();
    }

    @Override
    public void createChat(String chatName) throws Exception {

    }

    @Override
    public void getChatsFromUser(String host) throws Exception {

    }

    @Override
    public void sendMessageToChat(String host, String message) throws Exception {
        Session session;
        if ((session = sessions.get(host)) == null) {
            throw new Error("Host: " + host + " doesn't exists");
        }
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void sendMessageToGroupChat(String host, String chatName, String message) throws Exception {

    }
}
