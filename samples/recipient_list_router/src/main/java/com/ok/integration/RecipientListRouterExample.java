package com.ok.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.logging.Logger;

@SpringBootApplication
public class RecipientListRouterExample {
    Logger logger = Logger.getLogger(RecipientListRouterExample.class.getName());
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RecipientListRouterExample.class, args);

        CustomMessage customMessage = new CustomMessage("Hello from the main method!", 200);
        Message<CustomMessage> message = MessageBuilder.withPayload(customMessage)
                .setHeader("priority", 100)
                .build();

        MessageChannel c = context.getBean("inputChannel", MessageChannel.class);
        c.send(MessageBuilder.withPayload(message).build());

    }

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel loggingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel processingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel auditChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow recipientListRouterFlow() {
        return flow -> flow
                .channel("inputChannel")
                .routeToRecipients(r -> r
                        .recipient(loggingChannel())
                        .recipient(processingChannel())
                        .recipient(auditChannel()));
    }

    @Bean
    public IntegrationFlow loggingFlow() {
        return flow -> flow
                .channel(loggingChannel())
                .handle(message -> System.out.println("Logging: " + message.getPayload()));
    }

    @Bean
    public IntegrationFlow processingFlow() {
        return flow -> flow
                .channel(processingChannel())
                .handle(message -> System.out.println("Processing: " + message.getPayload()));
    }

    @Bean
    public IntegrationFlow auditFlow() {
        return flow -> flow
                .channel(auditChannel())
                .handle(message -> System.out.println("Auditing: " + message.getPayload()));
    }

/*    @MessagingGateway(defaultRequestChannel = "inputChannel")
    public interface MyGateway {
        void sendCustomMessage(@Header("priorityString") String priorityString, Message<CustomMessage> message);
    }

    @MessagingGateway(defaultRequestChannel = "inputChannel1")
    public interface MyGateway1 {
        void sendCustomMessage(@Header("priorityString") String priorityString, Message<CustomMessage> message);
    }

    @MessagingGateway(defaultRequestChannel = "inputChannel2")
    public interface MyGateway2 {
        void sendCustomMessage(@Header("priorityString") String priorityString, Message<CustomMessage> message);
    }*/

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


