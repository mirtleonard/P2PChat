package com.tora.handlers;

import com.tora.Connection;
import com.tora.ui.ConsoleClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleRequestHandler implements IRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleRequestHandler.class);

    private final ConsoleClient client;

    public ConsoleRequestHandler(ConsoleClient client) {
        this.client = client;
    }

    @Override
    public synchronized void handle(JSONObject request, Connection connection) {
        logger.info("Handling request {}", request.toString());
        JSONObject header = new JSONObject(request.getString("header"));
        if (!header.has("type")) {
            logger.info("JSON object doesn't have type");
            return;
        }

        if ("terminate".equals(header.get("type"))) {
            terminateConnection(connection);
        } else if ("connect".equals(header.get("type"))) {
            client.showConsole("connected with" + connection.getSocket().getInetAddress().getHostAddress());
        } else if ("message".equals(header.get("type"))) {
            client.showConsole((String) request.get("body"));
        }
    }

    private void terminateConnection(Connection connection) {
        connection.terminate();
    }
}
