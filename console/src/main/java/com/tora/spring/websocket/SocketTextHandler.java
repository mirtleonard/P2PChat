package com.tora.spring.websocket;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class SocketTextHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
        System.out.println("Handling text: " + jsonObject.toString());
        session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
    }

}
