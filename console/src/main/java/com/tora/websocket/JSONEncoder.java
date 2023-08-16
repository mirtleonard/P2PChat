package com.tora.websocket;

import org.json.JSONObject;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JSONEncoder implements Encoder.Text<JSONObject>{

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(JSONObject jsonObject) throws EncodeException {
        System.out.println("Encode " + jsonObject.toString());
        return jsonObject.toString();
    }
}
