package com.tora.websocket;

import com.tora.configuration.WebSocketContainers;
import jakarta.websocket.server.PathParam;
import org.json.JSONObject;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/chat/{username}", decoders = JSONDecoder.class, encoders = JSONEncoder.class, configurator = ServerConfigurator.class)
@ClientEndpoint(encoders = JSONEncoder.class, decoders = JSONDecoder.class)
public class WebSocketEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private final Map<String, Session> sessions;
    private Session session;
    private String userAddress;

    protected WebSocketContainer container;

    public WebSocketEndpoint() {
        container = ContainerProvider.getWebSocketContainer();
        sessions = WebSocketContainers.sessions;
    }

    @OnOpen
    public void onOpen (Session session, @PathParam("username") final String username) {
        System.out.println(session.toString());
        System.out.println(session.getUserProperties().keySet());
        this.session = session;
        if (session.getUserProperties().get("remote_ip") != null) {
            this.userAddress = (String) session.getUserProperties().get("remote_ip") + ":" + ((Integer) session.getUserProperties().get("remote_port")).toString();
        }
        sessions.computeIfAbsent(username, (k) -> session);
        System.out.println("[SERVER]: Handshake successful, session ID: " + session.getId());
    }
    @OnMessage
    public void onMessage (JSONObject json, Session session) throws EncodeException, IOException {
        System.out.println("server onMessage");
        try {
            if ("terminate".equals(json.get("terminate"))) {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "session closed"));
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
        sessions.remove(session.getRequestURI());
        System.out.println("[SERVER]: Session " + session.getId() + " closed, because " + closeReason);
    }
    @OnError
    public void onError(Session session, Throwable t) {
        logger.error("Error ", t);
    }

    public void connect(String remote_ip, String remote_port) throws URISyntaxException, DeploymentException, IOException {
        userAddress = remote_ip + ":" + remote_port;
        session = container.connectToServer(this, new URI("ws://" + userAddress + "/chat" + "/leonard" ));
    }
}