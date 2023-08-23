package com.tora.websocket;

import com.tora.configuration.WebSocketContainers;
import com.tora.utils.JSONBuilder;
import jakarta.websocket.server.PathParam;
import org.json.JSONObject;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@ServerEndpoint(value = "/chat/{username}", decoders = JSONDecoder.class, encoders = JSONEncoder.class, configurator = ServerConfigurator.class)
@ClientEndpoint(encoders = JSONEncoder.class, decoders = JSONDecoder.class)
@Component
@ComponentScan(basePackages = "com.tora.configuration")
public class WebSocketEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private final Map<String, Session> sessions;
    private Session session;
    private String alias;

    protected WebSocketContainer container;

    public WebSocketEndpoint() {
        container = ContainerProvider.getWebSocketContainer();
        this.sessions = WebSocketContainers.sessions;
    }

    private void notifyWebClient(Session session, JSONObject json) throws IOException {
        Session webApp = sessions.get("webApp");
        if (webApp != null && webApp != session && webApp.isOpen()) {
            json.getJSONObject("header").put("from", alias);
            webApp.getBasicRemote().sendText(json.toString());
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") final String username) throws Exception {
        this.session = session;
        if (username != null) {
            this.alias = username;
        }
        if (sessions.containsKey(alias)) {
            throw new Exception("Error: alias already exists");
        }
        sessions.put(alias, session);
        notifyWebClient(session,
                JSONBuilder.create()
                        .addHeader("type", "connection")
                        .addHeader("alias", alias)
                        .build()
        );
        System.out.println("[SERVER]: Handshake successful, session ID: " + session.getId());
    }

    @OnMessage
    public void onMessage(JSONObject json, Session session) throws EncodeException, IOException {
        System.out.println(json.get("body"));
        notifyWebClient(session, json);
    }
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(alias);
        System.out.println("[SERVER]: Session " + session.getId() + " closed, because " + closeReason);
    }
    @OnError
    public void onError(Session session, Throwable t) {
        logger.error("Error ", t);
    }

    public void connect(String alias, String remote_ip, String remote_port) throws URISyntaxException, DeploymentException, IOException {
        String userAddress = remote_ip + ":" + remote_port;
        this.alias = alias;
        session = container.connectToServer(this, new URI("ws://" + userAddress + "/chat" + "/leonard" ));
        sessions.putIfAbsent(alias, session);
    }
}