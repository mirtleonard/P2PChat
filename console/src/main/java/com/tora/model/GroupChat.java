package com.tora.model;

import com.tora.utils.JSONBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupChat {
    private final String name;

    private final static Logger logger = LoggerFactory.getLogger(GroupChat.class);
    private final Map<String, Connection> subscribers;

    public GroupChat(String name) {
        this.name = name;
        this.subscribers = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addSubscriber(Connection connection) {
        String user = connection.getSocket().getInetAddress().getHostAddress() + ":" + connection.getSocket().getPort();
        logger.info("Adding {} to {}", user, name);
        subscribers.compute(user, (k, v) -> v == null || v.isTerminated() ? connection : v);
    }

    public void removeSubscriber(Connection connection) {
        subscribers.computeIfPresent(connection.getSocket().getInetAddress().getHostAddress()+ ":" + connection.getSocket().getPort(), (k, v) -> null);
    }

    public void sendMessage(Connection con, String message) {
        synchronized (subscribers) {
            subscribers.replaceAll((k, v) -> v.isTerminated() ? null : v);
            if (subscribers.containsKey(con.getSocket().getInetAddress().getHostAddress() + ":" + con.getSocket().getPort())) {
                JSONObject object = JSONBuilder.create().addHeader("type", "message")
                        .addHeader("from", con.getSocket().getInetAddress().getHostAddress())
                        .addHeader("chat_name", name).setBody(message).build();
                subscribers.forEach((key, val) -> {
                    logger.info("Sending to {}: {}", key, object);
                            try {
                                val.send(object);
                            } catch (IOException ignore) {
                            }
                        }
                );
            }
        }
    }


}
