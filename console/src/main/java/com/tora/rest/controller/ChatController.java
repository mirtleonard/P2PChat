package com.tora.rest.controller;

import com.tora.service.BasicSocketService;
import com.tora.service.IService;
import com.tora.service.WebSocketService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/chat")
@CrossOrigin(origins="http://localhost:5173")
//@CrossOrigin
public class ChatController {
    @Autowired
    private WebSocketService service;

    @GetMapping("/connections")
    public String[] connections() {
        return service.getConnections();
    }
    @PostMapping("/connect")
    public ResponseEntity<String> connectToChat(@RequestBody ConnectionDTO data) {
        try {
            service.connectToChat(data.alias, data.ip, data.port);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("ip not found ", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("connected", HttpStatus.OK);
    }

    @PostMapping("/create-group")
    public int createGroup(@RequestBody String groupName) {
        try {
            service.createChat(groupName);
        } catch (Exception ex) {
        }
        return Response.SC_ACCEPTED;
    }

    @PostMapping("/{id}/send-message")
    public ResponseEntity<String> sendMessage(@PathVariable(value="id") String host, @RequestBody String message) {
        try {
            service.sendMessageToChat(host, message);
        } catch (Exception ex) {
        }
        return new ResponseEntity<>("message sent!", HttpStatus.OK);
    }

    @DeleteMapping("{id}/close-connection")
    public ResponseEntity<String> closeConnection(@PathVariable(value="id") String host) {
        try {
            service.closeConnection("/" + host);
        } catch (Exception ex) {
        }
        return new ResponseEntity<>("connection closed!", HttpStatus.OK);
    }

    @GetMapping("/{id}/chats")
    public ResponseEntity<String> getChats(@PathVariable(value="id") String host) {
        try {
            service.getChatsFromUser("/" + host);
        } catch (Exception ex) {
        }
        return new ResponseEntity<>("chats sent to console!", HttpStatus.OK);
    }

    @PostMapping("/{id}/connect-to-chat")
    public ResponseEntity<String> connectToChat(@PathVariable(value="id") String host, @RequestBody String chatName) {
        try {
            service.connectToGroupChat("/" + host, chatName);
        } catch (Exception ex) {
        }
        return new ResponseEntity<>("connected to the chat!", HttpStatus.OK);
    }

    @PostMapping("/{id}/{chat}/send-message")
    public ResponseEntity<String> messageChat(@PathVariable(value="id") String host,
                                              @PathVariable(value="chat") String chatName,
                                              @RequestBody String message) {
        try {
            service.sendMessageToGroupChat("/" + host, chatName, message);
        } catch (Exception ex) {
        }
        return new ResponseEntity<>("message sent to the chat!", HttpStatus.OK);
    }


    private static class ConnectionDTO {
        public String ip, port, alias;
        ConnectionDTO(String alias, String host, String port) {
            this.ip = host;
            this.port = port;
            this.alias = alias;
        }
    }

}