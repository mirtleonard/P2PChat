package com.tora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Listener implements Callable<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);
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

    public void terminate(){
        logger.info("Listener terminating");
        terminated = true;

        try {
            socket.close();
        } catch (IOException e) {
            logger.error("exception {} {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public Integer call() {
        logger.info("start listening");
        try {
            socket = new ServerSocket(port);
            while (!terminated) {
                Socket s = socket.accept();
                logger.info("new connection {}", s.getInetAddress().toString());
                Connection connection = new Connection(s);
                newConnectionsQueue.put(connection);
            }
        } catch (Exception exception) {
            logger.error("exception {} {}", exception.getClass().getSimpleName(), exception.getMessage());
            System.exit(-1);
        }
        return 0;
    }
}