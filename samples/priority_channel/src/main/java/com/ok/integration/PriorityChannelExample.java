package com.ok.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@SpringBootApplication
public class PriorityChannelExample {
    static Logger logger = Logger.getLogger(PriorityChannelExample.class.getName());

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PriorityChannelExample.class, args);
        MessageSender sender = context.getBean(MessageSender.class);

        ExecutorService executorService = Executors.newCachedThreadPool();

        // Send messages with varying priorities asynchronously
        executorService.execute(() -> sender.sendMessage("Message with priority 1", 1));
        executorService.execute(() -> sender.sendMessage("Message with priority 5", 5));
        executorService.execute(() -> sender.sendMessage("Message with priority 3", 3));
        executorService.execute(() -> sender.sendMessage("Message with high priority 10", 10));
        executorService.execute(() -> sender.sendMessage("Message with low priority 0", 0));

        executorService.shutdown();
        logger.info("-->Finished sending messages.");
        context.close();
    }

    @Bean
    public PriorityChannel priorityChannel() {
        // Setup the channel with a custom comparator for the priority header
        Comparator<Message<?>> comparator = (m1, m2) -> {
            Integer priority1 = m1.getHeaders().get("priority", Integer.class);
            Integer priority2 = m2.getHeaders().get("priority", Integer.class);

            // Handle cases where the priority header might not be set
            if (priority1 == null) priority1 = 0;
            if (priority2 == null) priority2 = 0;

            return priority2.compareTo(priority1);  // Sort in descending order (higher priority first)
        };

        return new PriorityChannel(10, comparator);
    }

    @ServiceActivator(inputChannel = "priorityChannel")
    public void handleMessage(Message<String> message) {
        logger.info("-->Processed message with priority " + message.getHeaders());
        logger.info("-->Processed message with priority " + message.getHeaders().get("priority", Integer.class) + ": " + message.getPayload());
    }

    @Component
    public static class MessageSender {

        @Autowired
        private MessageChannel priorityChannel;

        public void sendMessage(String content, int priority) {
            Message<String> message = MessageBuilder.withPayload(content)
                    .setHeader("priority", priority)
                    .build();
            this.priorityChannel.send(message);
        }
    }
}