package com.ok.integration;

import com.ok.integration.message.LineItem;
import com.ok.integration.message.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
@IntegrationComponentScan
@EnableIntegration
public class CorrelationStrategyAggregatorExample {
    Logger logger = Logger.getLogger(CorrelationStrategyAggregatorExample.class.getName());

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CorrelationStrategyAggregatorExample.class, args);


        List<LineItem> items = Arrays.asList(
                new LineItem("Gadget1", 10, 3, 22),
                new LineItem("Gadget2", 3, 2, 22)
        );
        Order order = new Order(22, items);

        MessageChannel c = context.getBean("inputChannel", MessageChannel.class);
        c.send(MessageBuilder.withPayload(order).build());

//        context.close();
    }

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel processLineItemChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel aggregationChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

}