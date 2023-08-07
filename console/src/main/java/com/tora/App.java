package com.tora;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private final static Map<String, Connection> connections = new ConcurrentHashMap<>();

    public static class Connection implements Callable<Integer>
    {
        private LinkedBlockingQueue<JSONObject> requests;
        private final Socket socket;
        private final ObjectInputStream inputStream;
        private final ObjectOutputStream outputStream;
        private volatile boolean terminated;
        private final ServerProxy serverProxy;
        public Connection(Socket socket, ServerProxy serverProxy) throws IOException {
            this.socket = socket;
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.serverProxy = serverProxy;
            this.serverProxy.setConnection(this);

        }

        public Socket getSocket() {
            return socket;
        }

        public void terminate(){
            terminated = true;
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        public void write(String message) throws IOException {
            synchronized (outputStream) {
                outputStream.writeObject(message);
                outputStream.flush();
            }
        }

        public Object read() throws IOException, ClassNotFoundException {
            return inputStream.readObject();
        }

        @Override
        public Integer call() throws Exception {
            while(!terminated){
                serverProxy.handleReply(new JSONObject((String)inputStream.readObject()));
            }
            return 0;
        }
    }

    public static class Listener implements Callable<Integer> {
        private final int port;
        private volatile boolean terminated;

        private final Map<String, ServerProxy> connections;
        private final Client client;

        public Listener(int port, Map<String, ServerProxy> con, Client client){
            connections = con;
            this.port = port;
            this.client = client;
        }

        public void terminate(){
            terminated = true;
        }

        @Override
        public Integer call() throws Exception {
            ServerSocket socket = new ServerSocket(port);
            while(!terminated){
                Socket s = socket.accept();
                ServerProxy server = new ServerProxy(client);
                server.setConnection(new Connection(s, server));
                // TODO add connection params
                connections.putIfAbsent(s.getInetAddress().getHostAddress(), server);
            }
            socket.close();
            return 0;
        }
    }
}
