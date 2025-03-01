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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class CustomSplitterExample {
    Logger logger = Logger.getLogger(CustomSplitterExample.class.getName());

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CustomSplitterExample.class, args);


        List<OrderItem> items = Arrays.asList(
                new OrderItem("Widget", 5),
                new OrderItem("Gadget", 3)
        );
        Order order = new Order(items);

        MessageChannel c = context.getBean("inputChannel", MessageChannel.class);
        c.send(MessageBuilder.withPayload(order).build());

    }

    public List<OrderItem> splitOrderIntoItems(Order order) {
        return order.getItems();
    }

    @Bean
    public IntegrationFlow orderProcessingFlow(CustomHandler customHandler) {
        return flow -> flow
                .channel("inputChannel")
                .split(Order.class, this::splitOrderIntoItems)
                .handle(customHandler::processMessage);
    }

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

}

class Order {
    private final List<OrderItem> items;

    public Order(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}

class OrderItem {
    private final String itemName;
    private final int quantity;

    public OrderItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
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
