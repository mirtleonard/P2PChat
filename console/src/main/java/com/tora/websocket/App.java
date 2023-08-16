package com.tora.websocket;

import com.tora.Service;
import com.tora.ui.ConsoleClient;
import org.glassfish.tyrus.server.Server;
import org.springframework.boot.web.server.WebServer;

public class App {
    public static void main(String[] args) {
        WebService service = new WebService();
        ConsoleClient console = new ConsoleClient(service);
        Server server = new Server("localhost", 8080, "/chat", WebSocketServerEndpoint.class);
        //console.run();

    }
}
