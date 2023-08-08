package com.tora;

import com.tora.handlers.IRequestHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

public class Connection implements Callable<Integer> {
    private volatile boolean terminated;
    private final Socket socket;

    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private IRequestHandler handler;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(this.socket.getInputStream());
    }

    public void setHandler(IRequestHandler handler) {
        this.handler = handler;
        try {
            this.handler.handle(JSONBuilder
                    .create()
                    .addHeader("type", "connect")
                    .addHeader("host", socket.getInetAddress().getHostAddress())
                    .addHeader("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .build(), this);
        } catch (Exception ignore) {
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void terminate() {
        terminated = true;
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(JSONObject jsonObject) throws IOException {
        synchronized (outputStream) {
            outputStream.writeObject(jsonObject.toString());
            outputStream.flush();
        }
    }

    @Override
    public Integer call() {
        try {
            while (!terminated) {
                handler.handle(new JSONObject((String) inputStream.readObject()), this);
            }
        } catch (Exception e) {
            terminated = true;
            return -1;
        }
        return 0;
    }
}