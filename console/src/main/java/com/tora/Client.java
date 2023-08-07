package com.tora;

import com.tora.App.Listener;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private Map<String, ServerProxy> services = new ConcurrentHashMap<>();
    public void showConsole(String console) {
        System.out.println(console);
    }

    public void sendMessage(String to, String message) {
        services.get(to).send(message);
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        Listener listener = new Listener(8001, client.services, client);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(listener);
        Socket socket = new Socket("127.0.0.1", 8000);
        ServerProxy server = new ServerProxy(this);
        App.Connection connection = new App.Connection(socket);
        Scanner sc = new Scanner(System.in);
        while (true) {

            String to = sc.nextLine();
            String message = sc.nextLine();
            client.sendMessage(to, message);
        }
    }
}
