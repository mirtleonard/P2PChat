package com.tora;

import java.util.Map;

public class GroupChat {
    private final String name;
    private final Map<String, Connection> subscribers;
    public GroupChat(String name, Map<String, Connection> subscribers){
        this.name = name;
        this.subscribers = subscribers;
    }

    public String getName() {
        return name;
    }

    public void addSubscriber(Connection connection){

    }

    public void removeSubscriber(Connection connection){

    }

    public synchronized void sendMessage(Connection con, String message){

    }


}
