package com.tora;

import com.tora.App.Connection;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerProxy  {
    private Client client;
    private Connection connection;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void send(String message) {
        try {
            connection.write(JSONBuilder.create().setBody(message).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ServerProxy(Client client){
        this.client = client;
    }
    public void handleReply(JSONObject object) {
        //TODO
        client.showConsole(object.toString());
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection(){
        this.connection.terminate();
    }
}
