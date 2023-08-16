package com.tora.spring.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ClientWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Client connection opened");
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Client connection closed: {}" + status);
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Client received: {}" + message);
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("Client transport error: {}" + exception.getMessage());
    }
}
