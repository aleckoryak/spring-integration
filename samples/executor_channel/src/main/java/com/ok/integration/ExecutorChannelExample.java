package com.ok.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@SpringBootApplication
public class ExecutorChannelExample {
    static Logger logger = Logger.getLogger(ExecutorChannelExample.class.getName());
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExecutorChannelExample.class, args);
        MessageSender sender = context.getBean(MessageSender.class);

        // Send messages
        sender.sendMessage("Hello, World!");
        sender.sendMessage("Spring Integration is fun!");
        logger.info("-->Finished sending messages.");
    }

    @Bean
    public MessageChannel executorChannel() {
        return new ExecutorChannel(taskExecutor());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);  // Customize based on your requirements
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

    @ServiceActivator(inputChannel = "executorChannel")
    public void handleMessageOne(Message<String> message) {
        logger.info("-->Handler One processed: " + message.getPayload() + " Thread: " + Thread.currentThread().getName());
    }

    @ServiceActivator(inputChannel = "executorChannel")
    public void handleMessageTwo(Message<String> message) {
        logger.info("-->Handler Two processed: " + message.getPayload() + " Thread: " + Thread.currentThread().getName());
    }

    @Component
    public static class MessageSender {

        @Autowired
        private MessageChannel executorChannel;

        public void sendMessage(String content) {
            Message<String> message = MessageBuilder.withPayload(content).build();
            this.executorChannel.send(message);
        }
    }
}