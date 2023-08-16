package com.tora.websocket;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;


public class JSONDecoder implements Decoder.Text<JSONObject> {

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public JSONObject decode(String s) throws DecodeException {
        System.out.println("Decode " + s);
        return new JSONObject(s);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
