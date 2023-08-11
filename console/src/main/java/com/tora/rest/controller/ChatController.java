package com.tora.rest.controller;

import com.tora.Service;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private Service service;

    @GetMapping("/connections")
    public String[] connections() {
       return service.getAllConnections();
    }
    @PostMapping("/connect")
    public ResponseEntity<String> connectToIp(@RequestBody ConnectionDTO data) {
        try {
            service.connect(data.host, data.port);
        } catch (Exception e) {
            return new ResponseEntity<>("ip not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("connected", HttpStatus.OK);
    }

    @PostMapping("/create-group")
    public int createGroup(@RequestBody String groupName) {
        service.createChat(groupName);
        return Response.SC_ACCEPTED;
    }

   @PostMapping("/{id}/send-message")
   public ResponseEntity<String> sendMessage(@PathVariable(value="id") String host, @RequestBody String message) {
        service.sendMessage("/" + host, message);
        return new ResponseEntity<>("message sent!", HttpStatus.OK);
   }

   @DeleteMapping("{id}/close-connection")
   public ResponseEntity<String> sendMessage(@PathVariable(value="id") String host) {
        service.terminate("/" + host) ;
        return new ResponseEntity<>("connection closed!", HttpStatus.OK);
   }

   @GetMapping("/{id}/chats")
   public ResponseEntity<String> getChats(@PathVariable(value="id") String host) {
        service.getChatsFromUser("/" + host);
        return new ResponseEntity<>("chats sent to console!", HttpStatus.OK);
   }

   @PostMapping("/{id}/connect-to-chat")
   public ResponseEntity<String> connectToChat(@PathVariable(value="id") String host, @RequestBody String chatName) {
        service.connectToChat("/" + host, chatName);
        return new ResponseEntity<>("connected to the chat!", HttpStatus.OK);
   }

   @PostMapping("/{id}/{chat}/send-message")
   public ResponseEntity<String> messageChat(@PathVariable(value="id") String host,
                                             @PathVariable(value="chat") String chatName,
                                             @RequestBody String message) {
        service.sendMessageToChat("/" + host, chatName, message);
        return new ResponseEntity<>("message sent to the chat!", HttpStatus.OK);
   }


    private static class ConnectionDTO {
        public String host;
        public String port;
        ConnectionDTO(String host, String port) {
            this.host = host;
            this.port = port;
        }
    }

}