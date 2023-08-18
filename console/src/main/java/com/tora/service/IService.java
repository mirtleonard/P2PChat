package com.tora.service;

public interface IService {
    public void connectToGroupChat(String user, String chatName) throws Exception;
    public void connectToChat(String alias, String user, String port) throws Exception;
    public String[] getConnections();
    public void listenPort(int port) throws Exception;
    public void terminate() throws Exception;
    public void closeConnection(String user) throws Exception;
    public void createChat(String chatName) throws Exception;
    public void getChatsFromUser(String user) throws Exception;
    public void sendMessageToChat(String user, String message) throws Exception;
    public void sendMessageToGroupChat(String user, String chatName, String message) throws Exception;
}
