package com.tora.ui;


import com.tora.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleClient {
    private final Service service;

    private static final Logger logger = LoggerFactory.getLogger(ConsoleClient.class);

    public ConsoleClient(Service service) {
        this.service = service;
    }

    public void showConsole(String console) {
        logger.info(console);
        System.out.println(console);
        System.out.flush();
    }

    //TODO add status after giving a coomand
    //split the function into helpers
    public void run() {
        String[] command;
        String completeCommand;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.flush();
            completeCommand = in.nextLine().trim();
            command = completeCommand.split(" +");
            if ("connect".equals(command[0])) {
                if (command.length != 3) {
                    System.out.println("Wrong number of arguments");
                    continue;
                }
                try {
                    service.connect(command[1], command[2]);
                } catch (Exception e) {
                    System.out.println(e);
                }
                continue;
            }
            if ("printAll".equals(command[0])) {
                Arrays.stream(service.getAllConnections()).forEach(System.out::println);
                continue;
            }
            if ("msg".equals(command[0])) {
                if (command.length < 2) {
                    System.out.println("Missing arguments");
                    continue;
                }
                service.sendMessage(command[1], completeCommand
                        .replaceFirst("[^ ]* *", "").replaceFirst("[^ ]* *", ""));
                continue;
            }
            if ("terminate".equals(command[0])) {
                if (command.length != 2) {
                    System.out.println("Missing arguments");
                    continue;
                }
                service.terminate(command[1]);
                continue;
            }
            if ("create_chat".equals(command[0])){
                if(command.length != 2){
                    System.out.println("Missing chat name");
                    continue;
                }
                service.createChat(command[1]);
                continue;
            }
            if ("get_chats".equals(command[0])){
                if(command.length != 2){
                    System.out.println("Missing ip");
                    continue;
                }
                service.getChatsFromUser(command[1]);
                continue;
            }
            if ("connect_to_chat".equals(command[0])) {
                if (command.length != 3) {
                    System.out.println("Missing arguments(command, ip, chat_name");
                    continue;
                }
                service.connectToChat(command[1], command[2]);
                continue;
            }
            if ("msg_chat".equals(command[0])) {
                if (command.length < 4) {
                    System.out.println("Missing arguments(command, ip, chat_name");
                    continue;
                }
                service.sendMessageToChat(command[1], command[2], completeCommand
                        .replaceFirst("[^ ]* *", "")
                        .replaceFirst("[^ ]* *", "")
                        .replaceFirst("[^ ]* *", ""));
                continue;
            }
            /*if ("flood".equals(command[0])){
                if(command.length != 2){
            }*/
            if ("quit".equals(command[0])) {
                break;
            }
            System.out.println("Unknown command");
        }
    }

}
