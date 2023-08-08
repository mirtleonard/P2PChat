package com.tora;

import org.json.JSONObject;

public class JSONBuilder {
    private final JSONObject header = new JSONObject();
    private Object body = new Object();

    private JSONBuilder() {

    }

    public static JSONBuilder create() {
        return new JSONBuilder();
    }

    public JSONBuilder addHeader(String key, Object value) {
        header.put(key, value);
        return this;
    }

    public JSONBuilder setBody(Object content) {
        body = content;
        return this;
    }

    public JSONObject build() {
        JSONObject json = new JSONObject();
        json.put("header", header);
        json.put("body", body);

        return json;
    }

}

