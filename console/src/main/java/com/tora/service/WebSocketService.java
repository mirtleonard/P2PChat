package com.tora.service;

import com.tora.configuration.WebSocketContainers;
import com.tora.websocket.WebSocketEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class WebSocketService implements IService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

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
        System.out.println("works");
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

    }

    @Override
    public void createChat(String chatName) throws Exception {

    }

    @Override
    public void getChatsFromUser(String host) throws Exception {

    }

    @Override
    public void sendMessageToChat(String host, String message) throws Exception {

    }

    @Override
    public void sendMessageToGroupChat(String host, String chatName, String message) throws Exception {

    }
}
