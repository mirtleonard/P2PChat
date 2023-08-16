package com.tora;

import com.tora.ui.ConsoleClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@ServletComponentScan
@EnableWebSocket
@SpringBootApplication
public class Chat {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Chat.class, args);
        ConsoleClient client = (ConsoleClient) context.getBean("consoleClient");
        client.run();
    }
}
