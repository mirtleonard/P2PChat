package com.tora;

import com.tora.ui.ConsoleClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringRESTChat {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(SpringRESTChat.class, args);
        ConsoleClient client = (ConsoleClient) context.getBean("consoleClient");
        client.run();
    }
}
