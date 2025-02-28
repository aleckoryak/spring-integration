package com.ok.samples;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

import java.util.logging.Logger;

@SpringBootApplication
//@IntegrationComponentScan
public class DirectChannelExample {
    Logger logger = Logger.getLogger(DirectChannelExample.class.getName());
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DirectChannelExample.class, args);
        MyGateway gateway = context.getBean(MyGateway.class);
        CustomMessage customMessage = new CustomMessage("Hello from the main method!", 200);
        Message<CustomMessage> message = MessageBuilder.withPayload(customMessage)
                .setHeader("priority", 100)
                .build();

        gateway.sendCustomMessage("high",message);
    }

    @Bean
    public MessageChannel inputChannel1() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "inputChannel1")
    public interface MyGateway {
        void sendCustomMessage(@Header("priorityString") String priorityString, Message<CustomMessage> message);
    }

    @ServiceActivator(inputChannel = "inputChannel1")
    public void messageReceiver(Message<CustomMessage> message) {

        CustomMessage payload = message.getPayload();

        logger.info(" -->Received message with headers: " + message.getHeaders());
        logger.info(" -->Content: " + payload.getContent() + ", Code: " + payload.getCode());
    }

    static class CustomMessage {
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
}
