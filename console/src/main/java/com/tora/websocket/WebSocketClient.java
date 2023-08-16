package com.tora.websocket;

import com.tora.utils.JSONBuilder;
import org.glassfish.tyrus.client.ClientManager;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint(encoders = JSONEncoder.class, decoders = JSONDecoder.class)
public class WebSocketClient {
    private static CountDownLatch latch;

    @OnOpen
    public void onOpen (Session session) {
        System.out.println("[CLIENT]: Connection established..... \n[CLIENT]: Session ID: " + session.getId() );
        try {
            session.getBasicRemote().sendText(JSONBuilder.create().setBody("hello").build().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @OnMessage
    public void onMessage (JSONObject json, Session session) throws IOException, EncodeException {
        System.out.println("[SERVER RESPONSE]: " + json.toString());
        //session.getBasicRemote().sendText(JSONBuilder.create().setBody("works").build().put("terminate", "terminate").toString());
    }
    @OnClose
    public void onClose (Session session, CloseReason closeReason) {
        System.out.println("[CLIENT]: Session " + session.getId() + " close, because " + closeReason);
        latch.countDown();
    }
    @OnError
    public void onError (Session session, Throwable err) {
        System.out.println("[CLIENT]: Error!!!!!, Session ID: " + session.getId() + ", " + err.getMessage());
    }
    public static void main(String[] args) {
        latch = new CountDownLatch(1);
        ClientManager clientManager = ClientManager.createClient();
        URI uri = null;
        try {
            uri = new URI("ws://localhost:8080/chat");
            //uri = new URI("ws://localhost:8080/demoApp");
            clientManager.connectToServer(WebSocketClient.class, uri);
            latch.await();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}