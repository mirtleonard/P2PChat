package com.tora.websocket;

public interface IService {
    public void connectToGroupChat(String host, String chatName) throws Exception;
    public void connectToChat(String host, String port) throws Exception;
    public String[] getConnections();
    public void listenPort(int port) throws Exception;
    public void terminate() throws Exception;
    public void closeConnection(String host) throws Exception;
    public void createChat(String chatName) throws Exception;
    public void getChatsFromUser(String host) throws Exception;
    public void sendMessageToChat(String host, String message) throws Exception;
    public void sendMessageToGroupChat(String host, String chatName, String message) throws Exception;
}
