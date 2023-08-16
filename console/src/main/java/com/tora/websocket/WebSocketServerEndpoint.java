package com.tora.websocket;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
@ServerEndpoint(value = "/demoApp", decoders = JSONDecoder.class, encoders = JSONEncoder.class)
public class WebSocketServerEndpoint {
    private Session session;
    private static Set<WebSocketServerEndpoint> chatEndpoints
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    @OnOpen
    public void onOpen (Session session) {
        chatEndpoints.add(this);
        System.out.println("[SERVER]: Handshake successful!!!!! - Connected!!!!! - Session ID: " + session.getId());
    }
    @OnMessage
    public void onMessage (JSONObject json, Session session) throws EncodeException, IOException {
        System.out.println("server onMessage");
        try {
            if ("terminate".equals(json.get("terminate"))) {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Successfully session closed....."));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(json.toString());
        session.getBasicRemote().sendText(json.toString());
    }
    @OnClose
    public void onClose (Session session, CloseReason closeReason) {
        chatEndpoints.remove(this);
        System.out.println("[SERVER]: Session " + session.getId() + " closed, because " + closeReason);
    }
}