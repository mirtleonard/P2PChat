package com.tora;

import com.tora.handlers.ConnectionHandler;
import com.tora.handlers.ConsoleRequestHandler;
import com.tora.ui.ConsoleClient;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws Exception {
        Map<String, GroupChat> groupChats = new ConcurrentHashMap<>();

        BlockingQueue<Connection> connectionBlockingQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Map<String, Connection> connections = new ConcurrentHashMap<>();

        Service service = new Service(connections, connectionBlockingQueue);
        service.setGroupChats(groupChats);
        ConsoleClient consoleClient = new ConsoleClient(service);
        ConsoleRequestHandler consoleRequestHandler = new ConsoleRequestHandler(consoleClient);
        consoleRequestHandler.setGroupChats(groupChats);

        UDPBroadcast brodcast = new UDPBroadcast(connectionBlockingQueue);
        service.setBrodcast(brodcast);
        executorService.submit(brodcast);

        ConnectionHandler connectionHandler = new ConnectionHandler(connectionBlockingQueue,
                connections,
                consoleRequestHandler,
                executorService);

        Scanner scanner = new Scanner(System.in);
        int port;
        System.out.print("Listening port: ");
        try {
            port = scanner.nextInt();
            brodcast.setPort(port);
            connectionHandler.listen(port);
        } catch (InputMismatchException e) {
            System.out.println("Port should be a number");
            System.out.println("Terminating...");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Terminating...");
            return;
        }

        consoleClient.run();

        connectionHandler.shutdown();
    }
}
