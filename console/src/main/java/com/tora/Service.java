package com.tora;

import com.tora.handlers.IRequestHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Service {
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
        if (connections.putIfAbsent(connection.getSocket().getInetAddress().toString(), connection) == null) {
            connection.setHandler(requestHandler);
            executorService.submit(connection);
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
}
