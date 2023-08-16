package com.tora.configuration;


import com.tora.service.IService;
import com.tora.service.WebSocketService;
import jakarta.websocket.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketContainers {

    public static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    public static final IService service = new WebSocketService();
}
