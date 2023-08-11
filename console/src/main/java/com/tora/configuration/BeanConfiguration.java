package com.tora.configuration;

import com.tora.model.Connection;
import org.springframework.cglib.core.Block;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class BeanConfiguration {

    @Bean(name="connections")
    public Map<String, Connection> getGetConnections() {
        return new HashMap<>();
    }
    @Bean(name="pendingConnections")
    public BlockingQueue<Connection> getBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

}
