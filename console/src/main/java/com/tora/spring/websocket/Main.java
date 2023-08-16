package com.tora.spring.websocket;

import com.tora.ui.ConsoleClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context =  SpringApplication.run(Main.class, args);
        ConsoleClient client = (ConsoleClient) context.getBean("consoleClient");
        client.run();
    }
}