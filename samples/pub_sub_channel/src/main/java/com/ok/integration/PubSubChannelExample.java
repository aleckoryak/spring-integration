package com.ok.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.logging.Logger;

@SpringBootApplication
public class PubSubChannelExample {
    static Logger logger = Logger.getLogger(PubSubChannelExample.class.getName());
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PubSubChannelExample.class, args);
        MyGateway gateway = context.getBean(MyGateway.class);

        CustomMessage customMessage = new CustomMessage("Hello from the main method!", 200);
        Message<CustomMessage> message = MessageBuilder.withPayload(customMessage)
                .setHeader("priority", 100)
                .build();
        logger.info("--> send message");
        gateway.sendToPubSub(message);
    }

    @Bean
    public PublishSubscribeChannel pubSubChannel() {
        return new PublishSubscribeChannel();
    }

    @ServiceActivator(inputChannel = "pubSubChannel")
    public void subscriberOne(Message<CustomMessage> message) {
        logger.info("-->Subscriber One received: " + message.getPayload());
    }

    @ServiceActivator(inputChannel = "pubSubChannel")
    public void subscriberTwo(Message<CustomMessage> message) {
        logger.info("-->Subscriber Two received: " + message.getPayload());
    }

    @ServiceActivator(inputChannel = "pubSubChannel")
    public void subscriberThree(Message<CustomMessage> message ) {
        logger.info("-->Subscriber Three received: " + message.getPayload());
    }

    @MessagingGateway(defaultRequestChannel = "pubSubChannel")
    public interface MyGateway {
        void sendToPubSub(Message<CustomMessage> message);
    }
}

class CustomMessage {
    private String content;
    private int code;

    public CustomMessage(String content, int code) {
        this.content = content;
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public int getCode() {
        return code;
    }
}