package com.tora.service;

import com.tora.handlers.ConnectionHandler;
import com.tora.handlers.ConsoleRequestHandler;
import com.tora.model.GroupChat;
import com.tora.utils.JSONBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BasicSocketService implements IService {
    private static final Logger logger = LoggerFactory.getLogger(BasicSocketService.class);

    private final ConnectionHandler connectionHandler;

    private final ConsoleRequestHandler requestHandler;

    public void terminate() throws IOException, InterruptedException {
        connectionHandler.shutdown();
    }
    public BasicSocketService(ConnectionHandler connectionHandler, ConsoleRequestHandler requestHandler) {
        this.connectionHandler = connectionHandler;
        this.requestHandler = requestHandler;
    }

    public void listenPort(int port) throws Exception {
        connectionHandler.listen(port);
    }

    public void connectToChat(String host, String port) throws Exception {
        connectionHandler.addPendingConnection(host, port);
    }

    public String[] getConnections() {
        return connectionHandler.getConnections().keySet().toArray(String[]::new);
    }



    public void sendMessageToChat(String to, String content) {
        connectionHandler.getConnections().computeIfPresent(to, (key, value) ->
        {
            try {
                value.send(JSONBuilder.create().addHeader("type", "message").setBody(content).build());
            } catch (IOException ignore) {
            }
            return value;
        });
    }

    public void closeConnection(String connection) {
        connectionHandler.getConnections().computeIfPresent(connection,(key, value) ->{
           try{
               value.send(JSONBuilder.create().addHeader("type", "terminate").build());
               value.terminate();
           }catch (IOException ignore){
           }
           return value;
        });
    }


    public void createChat(String name) {
        requestHandler.getGroupChats().putIfAbsent(name, new GroupChat(name));
    }

    public void removeChat(String name){
        requestHandler.getGroupChats().remove(name);
    }

    public void getChatsFromUser(String host){
        connectionHandler.getConnections().computeIfPresent(host, (key, value) ->
        {
            try {
                value.send(JSONBuilder.create().addHeader("type", "get_chats").build());
            } catch (IOException ignore) {
            }
            return value;
        });
    }

    public void connectToGroupChat(String host, String chatName) {
        connectionHandler.getConnections().computeIfPresent(host, (key, value) ->
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
    public void sendMessageToGroupChat(String host, String chatName, String content) {
        connectionHandler.getConnections().computeIfPresent(host, (key, value) ->
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
