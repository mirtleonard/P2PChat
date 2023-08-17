package com.tora.ui;
import com.tora.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ConsoleClient {
    private final IService service;

    private static final Logger logger = LoggerFactory.getLogger(ConsoleClient.class);

    public ConsoleClient(@Autowired IService service) {
        this.service = service;
    }

    public void run() throws Exception {
        Scanner in = new Scanner(System.in);
        String[] command;
        String completeCommand;
        while (true) {
            try {
                System.out.flush();
                completeCommand = in.nextLine().trim();
                command = completeCommand.split(" +");
                if ("connect".equals(command[0])) {
                    if (command.length != 3) {
                        System.out.println("Wrong number of arguments");
                        continue;
                    }
                    try {
                        service.connectToChat(command[1], command[2]);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    continue;
                }
                if ("print-connections".equals(command[0])) {
                    Arrays.stream(service.getConnections()).forEach(System.out::println);
                    continue;
                }
                if ("msg".equals(command[0])) {
                    if (command.length < 2) {
                        System.out.println("Missing arguments");
                        continue;
                    }
                    service.sendMessageToChat(command[1], completeCommand
                            .replaceFirst("[^ ]* *", "").replaceFirst("[^ ]* *", ""));
                    continue;
                }
                if ("close-connection".equals(command[0])) {
                    if (command.length != 2) {
                        System.out.println("Missing arguments");
                        continue;
                    }
                    service.closeConnection(command[1]);
                    continue;
                }
                if ("create-chat".equals(command[0])) {
                    if (command.length != 2) {
                        System.out.println("Missing chat name");
                        continue;
                    }
                    service.createChat(command[1]);
                    continue;
                }
                if ("get-chats".equals(command[0])) {
                    if (command.length != 2) {
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
                    service.sendMessageToGroupChat(command[1], command[2], completeCommand
                            .replaceFirst("[^ ]* *", "")
                            .replaceFirst("[^ ]* *", "")
                            .replaceFirst("[^ ]* *", ""));
                    continue;
                }
                if ("quit".equals(command[0])) {
                    break;
                }
                System.out.println("Unknown command");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        service.terminate();
    }

    private boolean readValidPort(Scanner in) {
        System.out.print("Listening port: ");
        try {
            service.listenPort(in.nextInt());
        } catch (InputMismatchException e) {
            System.out.println("Port should be a number");
            System.out.println("Terminating...");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Terminating...");
            return false;
        }
        return true;
    }

}
