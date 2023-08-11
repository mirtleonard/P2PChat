package com.tora.handlers;

import com.tora.model.Connection;
import org.json.JSONObject;

public interface IRequestHandler {
    void handle(JSONObject request, Connection connection) throws Exception;
}
