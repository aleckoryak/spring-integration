package com.ok.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@SpringBootApplication
public class QueueChannelExample {
    static Logger logger = Logger.getLogger(QueueChannelExample.class.getName());
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(QueueChannelExample.class, args);
        MessageSender sender = context.getBean(MessageSender.class);

        // Send some messages
        sender.sendMessage("Hello World!");
        sender.sendMessage("This is a QueueChannel example.");
        sender.sendMessage("Spring Integration is powerful!");
        logger.info("-->Finished sending messages.");
    }

    @Bean
    public MessageChannel queueChannel() {
        return new QueueChannel(10);
    }

    @Bean
    public PollerMetadata poller() {
        PeriodicTrigger trigger = new PeriodicTrigger(1000); // polls every 1000 milliseconds (1 second)
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(trigger);
        return pollerMetadata;
    }

    @ServiceActivator(inputChannel = "queueChannel", poller = @Poller("poller"))
    public void receiveMessage(Message<String> message) {
        logger.info("-->Received: " + message.getPayload());
    }

    @Component
    public static class MessageSender {

        @Autowired
        private MessageChannel queueChannel;

        public void sendMessage(String content) {
            Message<String> message = MessageBuilder.withPayload(content)
                    .build();
            this.queueChannel.send(message);
        }
    }
}