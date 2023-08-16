package com.tora.configuration;

import com.tora.model.Connection;
import com.tora.service.IService;
import com.tora.service.WebSocketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.websocket.Session;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class BeanConfiguration {
    private final IService service;
    private final Map<String, Session> sessions;
    public BeanConfiguration() {
        service = new WebSocketService();
        sessions = new ConcurrentHashMap<>();
    }

    @Bean(name="connections")
    public Map<String, Connection> getGetConnections() {
        return new HashMap<>();
    }
    @Bean(name="pendingConnections")
    public BlockingQueue<Connection> getBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }
    @Bean(name="sessions")
    public Map<String, Session> getSessions() {
        return sessions;
    }

    @Bean(name="service")
    public IService getService() {
        return service;
    }
}
