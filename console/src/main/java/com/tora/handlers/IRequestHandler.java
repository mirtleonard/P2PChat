package com.tora.handlers;

import org.json.JSONObject;

public interface IRequestHandler {
    void handle(JSONObject request) throws Exception;
}
