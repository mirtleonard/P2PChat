package com.tora;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

public class GroupChat {
    private final String name;
    private final Map<String, Connection> subscribers;

    public GroupChat(String name) {
        this.name = name;
        this.subscribers = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addSubscriber(Connection connection) {
        subscribers.compute(connection.getSocket().getInetAddress().getHostAddress(), (k, v) -> v == null || v.isTerminated() ? connection : v);
    }

    public void removeSubscriber(Connection connection) {
        subscribers.computeIfPresent(connection.getSocket().getInetAddress().getHostAddress(), (k, v) -> null);
    }

    public void sendMessage(Connection con, String message) {
        synchronized (subscribers) {
            subscribers.replaceAll((k, v) -> v.isTerminated() ? null : v);
            if (subscribers.containsKey(con.getSocket().getInetAddress().getHostAddress())) {
                JSONObject object = JSONBuilder.create().addHeader("type", "message")
                        .addHeader("from", con.getSocket().getInetAddress().getHostAddress())
                        .addHeader("chatname", name).setBody(message).build();
                subscribers.forEach((key, val) -> {
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
