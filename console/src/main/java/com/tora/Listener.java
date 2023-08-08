package com.tora;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Listener implements Callable<Integer> {
    private final int port;

    public int getPort() {
        return port;
    }

    private volatile boolean terminated;
    private final BlockingQueue<Connection> newConnectionsQueue;
    private ServerSocket socket;

    public Listener(int port, BlockingQueue<Connection> newConnections) {
        this.newConnectionsQueue = newConnections;
        this.port = port;
    }

    public void terminate() throws IOException {
        terminated = true;
        socket.close();
    }

    @Override
    public Integer call() {
        try {
            socket = new ServerSocket(port);
            while (!terminated) {
                Socket s = socket.accept();
                Connection connection = new Connection(s);
                newConnectionsQueue.put(connection);
            }
        } catch (Exception ignored) {
        }
        return 0;
    }
}