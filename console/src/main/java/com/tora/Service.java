package com.tora;

import com.tora.handlers.IRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    private final BlockingQueue<Connection> pendingConections;
    private UDPBroadcast brodcast;

    public Service(Map<String, Connection> connections, BlockingQueue<Connection> queue) {
        this.connections = connections;
        pendingConections = queue;
    }

    public void setBrodcast(UDPBroadcast brodcast) {
        this.brodcast = brodcast;
    }
    public void sendToBrodcast(String message) {
        brodcast.sendPackage(message.getBytes());
    }
    private final Map<String, Connection> connections;

    public void connect(String host, String port) throws Exception {
        Socket socket = new Socket(host, Integer.parseInt(port));
        Connection connection = new Connection(socket);
        logger.info("Socket and connection created");
        pendingConections.put(connection);
        throw new Exception("Already connected to " + host);
    }

    public String[] getAllConnections() {
        return connections.keySet().toArray(String[]::new);
    }



    public void sendMessage(String to, String content) {
        connections.computeIfPresent(to, (key, value) ->
        {
            try {
                value.send(JSONBuilder.create().addHeader("type", "message").setBody(content).build());
            } catch (IOException ignore) {
            }
            return value;
        });
    }

    public void terminate(String connection){
        connections.computeIfPresent(connection,(key, value) ->{
           try{
               value.send(JSONBuilder.create().addHeader("type", "terminate").build());
               value.terminate();
           }catch (IOException ignore){
           }
           return value;
        });
    }

    private Map<String, GroupChat> groupChats;

    public void setGroupChats(Map<String, GroupChat> groupChats){
        this.groupChats = groupChats;
    }

    public void createChat(String name){
        groupChats.putIfAbsent(name, new GroupChat(name));
    }

    public void removeChat(String name){
        groupChats.remove(name);
    }

    public void getChatsFromUser(String host){
        connections.computeIfPresent(host, (key, value) ->
        {
            try {
                value.send(JSONBuilder.create().addHeader("type", "get_chats").build());
            } catch (IOException ignore) {
            }
            return value;
        });
    }

    public void connectToChat(String host, String chatName) {
        connections.computeIfPresent(host, (key, value) ->
        {
            try {
                value.send(
                        JSONBuilder.create()
                                .addHeader("type", "connect_to_chat")
                                .addHeader("chat_name", chatName).build()
                );
            } catch (IOException ignore) {
            }
            return value;
        });
    }
    public void sendMessageToChat(String host, String chatName, String content) {
        connections.computeIfPresent(host, (key, value) ->
        {
            try {
                value.send(
                        JSONBuilder.create()
                                .addHeader("type", "chat_message")
                                .addHeader("chat_name", chatName)
                                .setBody(content)
                                .build()
                );
            } catch (IOException ignore) {
            }
            return value;
        });
    }
}
