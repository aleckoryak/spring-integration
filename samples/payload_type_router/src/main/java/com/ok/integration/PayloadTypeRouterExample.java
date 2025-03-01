package com.ok.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@SpringBootApplication
public class PayloadTypeRouterExample {
    Logger logger = Logger.getLogger(PayloadTypeRouterExample.class.getName());

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PayloadTypeRouterExample.class, args);

        CustomMessage customMessage = new CustomMessage("Hello from the main method!", 200);
        Message<CustomMessage> message = MessageBuilder.withPayload(customMessage)
                .setHeader("priority", 100)
                .build();

        MessageChannel c = context.getBean("inputChannel", MessageChannel.class);
        c.send(MessageBuilder.withPayload("Hello, this is a string").build());
        c.send(MessageBuilder.withPayload(100).build());
        c.send(MessageBuilder.withPayload(22.55).build());
    }


    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel textProcessingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel numberProcessingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel defaultProcessingChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow payloadTypeRouterFlow() {
        return flow -> flow
                .channel(inputChannel())
                .<Object, Class<?>>route(Object::getClass, m -> m
                        .channelMapping(String.class, "textProcessingChannel")
                        .channelMapping(Integer.class, "numberProcessingChannel")
                        .defaultOutputChannel("defaultProcessingChannel"));
    }

    @Bean
    public IntegrationFlow loggingFlow() {
        return flow -> flow
                .channel(textProcessingChannel())
                .handle(message -> System.out.println("textProcessingChannel: " + message));
    }

    @Bean
    public IntegrationFlow processingFlow() {
        return flow -> flow
                .channel(numberProcessingChannel())
                .handle(message -> System.out.println("numberProcessingChannel: " + message.getPayload()));
    }

    @Bean
    public IntegrationFlow auditFlow() {
        return flow -> flow
                .channel(defaultProcessingChannel())
                .handle(message -> System.out.println("defaultProcessingChannel: " + message.getPayload()));
    }

}

class CustomMessage {
    private final String content;
    private final int code;

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

    @Override
    public String toString() {
        return "CustomMessage{" +
                "content='" + content + '\'' +
                ", code=" + code +
                '}';
    }
}

@Component
class CustomHandler {
    Logger logger = Logger.getLogger(CustomHandler.class.getName());

    public void processMessage(Message<?> message) {
        logger.info(" -->Received message with headers: " + message.getHeaders());
        logger.info(" -->Content: " + message.getPayload());
    }
}


