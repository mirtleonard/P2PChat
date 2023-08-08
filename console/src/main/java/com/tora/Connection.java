package com.tora;

import com.tora.handlers.IRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Callable;

public class Connection implements Callable<Integer> {
    Logger logger = LoggerFactory.getLogger(Connection.class);
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
                    .addHeader("type", "local_connect")
                    .addHeader("host", socket.getInetAddress().getHostAddress())
                    .addHeader("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .build(), this);
        } catch (Exception exception) {
            logger.error("on ");
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
            logger.error("while closing error {} {}", e.getClass().getSimpleName(), e.getMessage());
        }
        logger.info("connection {} closed", socket.getInetAddress().toString());
        try {
            this.handler.handle(JSONBuilder
                    .create()
                    .addHeader("type", "local_disconnect")
                    .addHeader("host", socket.getInetAddress().toString())
                    .addHeader("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .build(), this);
        }catch (Exception e){
            logger.error("{} {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public void send(JSONObject jsonObject) throws IOException {
        synchronized (outputStream) {
            logger.info("sending To: {} JsonObject: {}", socket.getInetAddress().toString(), jsonObject.toString());
            outputStream.writeObject(jsonObject.toString());
            outputStream.flush();
            logger.info("sent");
        }
    }

    @Override
    public Integer call() {
        while (!terminated) {
            try {
                logger.info("waiting for: {}", socket.getInetAddress().toString());
                JSONObject tmp = new JSONObject((String) inputStream.readObject());
                logger.info("getting From: {} JsonObject: {}", socket.getInetAddress().toString(), tmp);
                handler.handle(tmp, this);
            } catch (IOException e) {
                terminate();
                logger.error("from: {} error {} {}", socket.getInetAddress().toString(), e.getClass().getSimpleName(), e.getMessage());
                return -1;
            } catch (Exception ignore) {
            }
        }
        return 0;
    }
}