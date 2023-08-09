package com.tora;

import com.tora.handlers.IRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    private final ExecutorService executorService;

    public Service(Map<String, Connection> connections, ExecutorService executorService) {
        this.connections = connections;
        this.executorService = executorService;
    }

    private final Map<String, Connection> connections;

    private IRequestHandler requestHandler;

    public void setRequestHandler(IRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void connect(String host, String port) throws Exception {
        Socket socket = new Socket(host, Integer.parseInt(port));
        Connection connection = new Connection(socket);
        logger.info("Socket and connection created");
        if (connections.putIfAbsent(connection.getSocket().getInetAddress().toString() + ":" + connection.getSocket().getPort(), connection) == null) {
            logger.info("connection added");
            connection.setHandler(requestHandler);
            executorService.submit(connection);
            logger.info("connection running");
            return;
        }
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
