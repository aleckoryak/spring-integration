package com.ok.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@SpringBootApplication
public class RendezvousChannelExample {
    static Logger logger = Logger.getLogger(RendezvousChannelExample.class.getName());

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RendezvousChannelExample.class, args);
        MessageSender sender = context.getBean(MessageSender.class);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            logger.info("-->Sending a message...");
            sender.sendMessage("Hello, rendezvous!");
            logger.info("-->Message sent and received!");
        });
        executor.shutdown();
        context.close();
    }

    @Bean
    public MessageChannel rendezvousChannel() {
        return new RendezvousChannel();
    }

    @ServiceActivator(inputChannel = "rendezvousChannel")
    public void handleMessage(Message<String> message) {
        logger.info("-->Received message: " + message.getPayload());
        try {
            // Simulate processing delay
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Component
    public static class MessageSender {

        @Autowired
        private MessageChannel rendezvousChannel;

        public void sendMessage(String content) {
            Message<String> message = MessageBuilder.withPayload(content).build();
            this.rendezvousChannel.send(message);
        }
    }
}