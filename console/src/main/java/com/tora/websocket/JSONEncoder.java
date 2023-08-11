package com.tora.websocket;

import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JSONEncoder implements Encoder.Text<JSONObject>{

    //@Override
    public void encode(JSONObject json, OutputStream outputStream) throws EncodeException, IOException {
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(json.toString());
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(JSONObject jsonObject) throws EncodeException {
        return jsonObject.toString();
    }
}
