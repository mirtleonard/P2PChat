package com.tora.configuration;

import com.tora.websocket.WebSocketEndpoint;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Configuration
@ComponentScan(basePackages = "com.tora")
public class WebSocketConfig {
    @Bean
    public WebSocketEndpoint chatEndpointNew(){
        return new WebSocketEndpoint();
    }


    @Bean
    public ServerEndpointExporter endpointExporter(){
        return new ServerEndpointExporter();
    }

}

