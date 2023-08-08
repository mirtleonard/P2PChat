package com.tora.handlers;

import com.tora.Connection;
import com.tora.ui.ConsoleClient;
import org.json.JSONObject;

public class ConsoleRequestHandler implements IRequestHandler {
    private final ConsoleClient client;

    public ConsoleRequestHandler(ConsoleClient client) {
        this.client = client;
    }

    @Override
    public synchronized void handle(JSONObject request, Connection connection) throws Exception {
        client.showConsole(request.toString());
        JSONObject header = new JSONObject(request.getString("header"));

        if(header.has("type")){
            if("terminate".equals(header.get("type"))){
                terminateConnection(connection);
                return;
            }
        }
        if(header.has("type")){
            if("message".equals(header.get("type"))){
                client.showConsole((String) request.get("body"));
            }
        }
    }

    private void terminateConnection(Connection connection){
        connection.terminate();
    }
}
