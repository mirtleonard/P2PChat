package com.tora.handlers;

import com.tora.Connection;
import org.json.JSONObject;

public interface IRequestHandler {
    void handle(JSONObject request, Connection connection);
}
