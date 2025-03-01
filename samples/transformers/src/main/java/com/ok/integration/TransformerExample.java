package com.ok.integration;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.logging.Logger;

@SpringBootApplication
public class TransformerExample {
    Logger logger = Logger.getLogger(TransformerExample.class.getName());
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TransformerExample.class, args);

        CustomMessage customMessage = new CustomMessage("Hello from the main method!", 200);
        Message<CustomMessage> message = MessageBuilder.withPayload(customMessage)
                .setHeader("priority", 100)
                .build();

        MyGateway gateway = context.getBean(MyGateway.class);
        gateway.sendCustomMessage("high",message);
//--------------
        MyGateway1 gateway1 = context.getBean(MyGateway1.class);
        gateway1.sendCustomMessage("high",message);

        MyGateway2 gateway2 = context.getBean(MyGateway2.class);
        gateway2.sendCustomMessage("high",message);
    }

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inputChannel1() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inputChannel2() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow objectToJsonTransformationFlow(CustomHandler customHandler) {
        return flow -> flow
                .channel(inputChannel())
                .transform(new ObjectToJsonTransformer())
                .handle(customHandler::processMessage);
    }

    @Bean
    public IntegrationFlow headerEnricherFlow(CustomHandler customHandler) {
        return flow -> flow
                .channel(inputChannel1())
                .transform(new HeaderEnricher(Collections.singletonMap("newHeader",
                    new org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor<>("newValue"))))
                .handle(customHandler::processMessage);
    }


    @Bean
    public IntegrationFlow customTransformationFlow(MyTransformations transformations) {
        return flow -> flow
                .channel(inputChannel2())
                .<CustomMessage, String>transform(transformations::customTransformation)
                .handle(s-> logger.info("++++Processed payload: " + s));
    }

    @MessagingGateway(defaultRequestChannel = "inputChannel")
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
    }

}
@Component
class MyTransformations {
    public String customTransformation(CustomMessage input) {
        // Assuming you need a String result, perhaps for logging or further processing
        return "Transformed: " + input.getContent().toUpperCase() + ", Code: " + input.getCode();
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

@Component
class CustomHandler {
    Logger logger = Logger.getLogger(CustomHandler.class.getName());

    public void processMessage(Message<?> message) {
        logger.info(" -->Received message with headers: " + message.getHeaders());
        logger.info(" -->Content: " + message.getPayload());
    }
}


