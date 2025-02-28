package com.ok.integration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class CompositeFilterApplication {
    Logger logger = Logger.getLogger(CompositeFilterApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(CompositeFilterApplication.class, args);
    }

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow processMessageFlow(CustomHandler customHandler) {
        return flow -> flow.channel(inputChannel())
                .filter(new CompositeFilter(Arrays.asList(
                        new ContainsKeywordFilter("Spring"),
                        new MinimumLengthFilter(5)
                ), CompositeType.AND))
                .handle(customHandler::processMessage);
    }

    @Bean
    public CommandLineRunner runner(ApplicationContext ctx) {
        return args -> {
            MessageChannel channel = ctx.getBean("inputChannel", MessageChannel.class);
            channel.send(new GenericMessage<>("Hello Spring World"));  // Should be passed through
            channel.send(new GenericMessage<>("Spring"));               // Should not pass
            channel.send(new GenericMessage<>("Hello World, it's a beautiful day!")); // Should not pass
            logger.info("Finished sending messages.");
        };
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