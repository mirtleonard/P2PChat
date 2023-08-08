package com.tora.handlers;

import com.tora.Connection;
import com.tora.GroupChat;
import com.tora.JSONBuilder;
import com.tora.ui.ConsoleClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ConsoleRequestHandler implements IRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleRequestHandler.class);

    private final ConsoleClient client;

    public ConsoleRequestHandler(ConsoleClient client) {
        this.client = client;
    }

    @Override
    public synchronized void handle(JSONObject request, Connection connection) {
        logger.info("Handling request {}", request.toString());
        if (!request.has("header")) {
            logger.info("header missing");
            return;
        }
        JSONObject header = request.getJSONObject("header");
        if (!header.has("type")) {
            logger.info("JSON object doesn't have type");
            return;
        }

        if ("terminate".equals(header.get("type"))) {
            terminateConnection(connection);
        } else if ("local_connect".equals(header.get("type"))) {
            client.showConsole("connected with " + connection.getSocket().getInetAddress().getHostAddress());
        } else if ("local_disconnect".equals(header.get("type"))) {
            client.showConsole("disconnect from " + connection.getSocket().getInetAddress().getHostAddress());
        }

        if (request.has("body")) {
            if ("message".equals(header.get("type"))) {
                client.showConsole(request.get("body").toString());
            } else if ("get_chats_response".equals(header.get("type"))) {
                client.showConsole("chats " + request.get("body"));
            }
        }

        if (groupChats != null) {
            if ("get_chats".equals(header.get("type"))) {
                JSONObject object = JSONBuilder.create().addHeader("type", "get_chats_response")
                        .setBody(new JSONArray(groupChats.keySet())).build();
                try {
                    connection.send(object);
                } catch (Exception ignore) {
                }
            }
        }
    }

    private void terminateConnection(Connection connection) {
        connection.terminate();
    }

    private Map<String, GroupChat> groupChats;

    public void setGroupChats(Map<String, GroupChat> groupChats) {
        this.groupChats = groupChats;
    }

    private void subscribeToGroupChat(String nameChat, Connection connection) {
        groupChats.computeIfPresent(nameChat, (k, v) -> {
            v.addSubscriber(connection);
            return v;
        });
    }

    private void unsubscribeFromGroupChat(String nameChat, Connection connection) {
        groupChats.computeIfPresent(nameChat, (k, v) -> {
            v.removeSubscriber(connection);
            return v;
        });
    }

    private void sendToGroupChat(String nameChat, Connection connection, String message) {
        groupChats.computeIfPresent(nameChat, (k, v) -> {
            v.sendMessage(connection, message);
            return v;
        });
    }
}
