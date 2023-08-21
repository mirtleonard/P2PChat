package com.tora.service;

import com.tora.configuration.WebSocketContainers;
import com.tora.utils.JSONBuilder;
import com.tora.websocket.WebSocketEndpoint;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class WebSocketService implements IService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    private final Map<String, Session> sessions =  WebSocketContainers.sessions;


    public String createNotification(String message) {
        return JSONBuilder.create()
                .addHeader("type", "notification")
                .setBody(message).build().toString();
    }

    public void notifyWeb(String action) {
        try {
            sessions.get("webApp").getBasicRemote().sendText(action);
        } catch (Exception ex) {
            logger.info("Error: couldn't notify web app: " + ex.getMessage());
        }
    }

    @Override
    public void connectToGroupChat(String host, String chatName) throws Exception {

    }

    public void connectToChat(String alias, String host, String port) {
        WebSocketEndpoint client = new WebSocketEndpoint();
        try {
            client.connect(alias, host, port);
            notifyWeb(JSONBuilder.create()
                    .addHeader("alias", alias)
                    .addHeader("type", "connection")
                    .build().toString());
            notifyWeb(createNotification("Connected" + alias));
        } catch (Exception e) {
            notifyWeb(createNotification("Coudn't connect with " + alias));
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
    public void sendMessageToChat(String host, String content) throws Exception {
        Session session;
        if ((session = sessions.get(host)) == null) {
            throw new Error("Host: " + host + " doesn't exists");
        }
        session.getBasicRemote().sendText(
                JSONBuilder.create().addHeader("type", "message").setBody(content).build().toString()
        );
        notifyWeb(JSONBuilder.create()
                .addHeader("type", "message")
                .addHeader("from", "me")
                .addHeader("to", host)
                .setBody(content).build().toString());
    }

    @Override
    public void sendMessageToGroupChat(String host, String chatName, String message) throws Exception {
    }
}
