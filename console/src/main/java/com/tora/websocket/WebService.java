package com.tora.websocket;
import com.tora.spring.websocket.ClientWebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

public class WebService implements IService {
    private Map<String, Session> connections = new HashMap<>();

    @Override
    public void connectToGroupChat(String host, String chatName) throws Exception {

    }

    public void connectToChat(String host, String port) {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(new StandardWebSocketClient(), new ClientWebSocketHandler(), "ws://localhost:8080/chat");
        manager.start();

        /*CountDownLatch latch = new CountDownLatch(1);
        ClientManager clientManager = ClientManager.createClient();
        URI uri = null;
        try {
            uri = new URI("ws://" + host + ":" + port + "/chat");
            clientManager.connectToServer(WebSocketClient.class, uri);
            latch.await();
        } catch (URISyntaxException | DeploymentException | InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
    }

    @Override
    public String[] getConnections() {
        return connections.keySet().toArray(new String[0]);
    }

    @Override
    public void listenPort(int port) throws Exception {

    }

    @Override
    public void terminate() throws Exception {

    }

    @Override
    public void closeConnection(String host) throws Exception {

    }

    @Override
    public void createChat(String chatName) throws Exception {

    }

    @Override
    public void getChatsFromUser(String host) throws Exception {

    }

    @Override
    public void sendMessageToChat(String host, String message) throws Exception {

    }

    @Override
    public void sendMessageToGroupChat(String host, String chatName, String message) throws Exception {

    }
}
