package com.tora.handlers;

import com.tora.ui.ConsoleClient;
import org.json.JSONObject;

public class ConsoleRequestHandler implements IRequestHandler {
    ConsoleClient client;

    public ConsoleRequestHandler(ConsoleClient client) {
        this.client = client;
    }

    @Override
    public synchronized void handle(JSONObject request) throws Exception {
        client.showConsole(request.toString());
    }
}
