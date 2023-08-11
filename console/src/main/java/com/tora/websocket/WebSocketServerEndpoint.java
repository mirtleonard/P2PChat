package com.tora.websocket;

import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


import java.io.IOException;

@ServerEndpoint(value = "/demoApp", decoders = JSONDecoder.class, encoders = JSONEncoder.class)
public class WebSocketServerEndpoint {
    @OnOpen
    public void onOpen (Session session) {
        System.out.println("[SERVER]: Handshake successful!!!!! - Connected!!!!! - Session ID: " + session.getId());
    }
    @OnMessage
    public JSONObject onMessage (JSONObject json, Session session) throws EncodeException, IOException {
        System.out.println("server");
        if ("terminate".equals(json.get("terminate"))) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Successfully session closed....."));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        session.getBasicRemote().sendObject(json);
        return json;
    }@OnClose
    public void onClose (Session session, CloseReason closeReason) {
        System.out.println("[SERVER]: Session " + session.getId() + " closed, because " + closeReason);
    }
}