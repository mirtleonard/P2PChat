package com.tora;

import org.json.JSONObject;

public class JSONBuilder {
    private final JSONObject header = new JSONObject();
    private String body;

    private JSONBuilder(){

    }

    public static JSONBuilder create(){
        return new JSONBuilder();
    }

    public JSONBuilder addHeader(String key, String value) {
       header.put(key, value);
       return this;
    }

    public JSONBuilder setBody(String content) {
       body = content;
       return this;
    }

    public String build() {
        JSONObject json = new JSONObject();
        json.put("header", header.toString());
        json.put("body", body);
        return json.toString();
    }

}

