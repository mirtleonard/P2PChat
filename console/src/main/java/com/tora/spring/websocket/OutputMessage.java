package com.tora.spring.websocket;

public class OutputMessage {

    String from, text, time;

    public OutputMessage(String from, String text, String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }

}
