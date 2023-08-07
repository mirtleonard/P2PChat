package com.tora.connection;

import java.net.Socket;

public class Connection implements Runnable{
    private Socket connectionSocket;
    private String name;
    private Connection() {};
    public void write(Object message) {};
    public Object read() {return  null;};

    public Socket getConnectionSocket() {
        return connectionSocket;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {

    }

    public static class ConnectionBuilder{
        private final Connection connection = new Connection();
        public ConnectionBuilder setSocket(Socket s){
            this.connection.connectionSocket = s;
            return this;
        }

        public ConnectionBuilder setName(String name){
            connection.name = name;
            return this;
        }

        public Connection build(){
            return connection;
        }
    }

}
