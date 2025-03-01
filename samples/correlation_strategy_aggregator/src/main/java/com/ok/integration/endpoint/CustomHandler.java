package com.ok.integration.endpoint;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CustomHandler {
    Logger logger = Logger.getLogger(CustomHandler.class.getName());

    public void processMessage(Message<?> message) {
        logger.info(" -->Received message with headers: " + message.getHeaders());
        logger.info(" -->Content: " + message.getPayload());
    }
}
