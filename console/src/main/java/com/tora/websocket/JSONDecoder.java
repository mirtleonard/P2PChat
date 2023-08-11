package com.tora.websocket;

import org.json.JSONObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class JSONDecoder implements Decoder.Text<JSONObject> {
    //@Override
    public JSONObject decode(InputStream inputStream) throws DecodeException, IOException {
        ObjectInputStream input = new ObjectInputStream(inputStream);
        Object o;
        try {
            o = input.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(o.toString());
        return new JSONObject(o);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public JSONObject decode(String s) throws DecodeException {
        System.out.println(s);
        return new JSONObject(s);
    }

    @Override
    public boolean willDecode(String s) {
        System.out.println( "aici " + s);
        return true;
    }
}
