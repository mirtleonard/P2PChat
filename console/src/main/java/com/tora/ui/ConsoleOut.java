package com.tora.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleOut {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleClient.class);

    public void showConsole(String console) {
        logger.info(console);
        System.out.println(console);
        System.out.flush();
    }
}
