package com.tora.handlers;

import com.tora.Connection;
import com.tora.GroupChat;
import com.tora.JSONBuilder;
import com.tora.ui.ConsoleClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
            } else if ("chat_message".equals((header.get("type")))) {
                client.showConsole(sendToGroupChat(header.getString("chat_name"),
                        connection, request.getString("body")));
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

            if ("connect_to_chat".equals(header.get("type"))) {
                try {
                    connection.send(getConnectToChatResponse(header.get("chat_name"), connection));
                } catch (IOException ignore) {
                }
            }

        }
    }

    private JSONObject getConnectToChatResponse(Object chatName, Connection connection) {
        if (chatName == null || !groupChats.containsKey((String) chatName)) {
            client.showConsole("Couldn't connect to  " + chatName);
            return JSONBuilder.create()
                    .addHeader("type", "connect_to_chat_response")
                    .addHeader("chat_name", chatName)
                    .setBody("chat name: " + chatName + " not found").build();
        }
        groupChats.get(chatName).addSubscriber(connection);
        client.showConsole("Connected to  " + chatName);
        return JSONBuilder.create()
                .addHeader("type", "connect_to_chat_response")
                .addHeader("chat_name", chatName)
                .addHeader("status", "OK")
                .setBody("connected to the chat")
                .build();
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

    private String sendToGroupChat(String chatName, Connection connection, String message) {
        if (!groupChats.containsKey(chatName)) {
            return "Group doesn't exists";
        }
        groupChats.get(chatName).sendMessage(connection, message);
        return "Message sent";
    }
}
