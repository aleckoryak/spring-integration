package com.ok.integration.endpoint;

import com.ok.integration.message.LineItem;
import com.ok.integration.message.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Component
public class OrderFlows {

    @Bean
    public IntegrationFlow orderProcessingFlow() {
        return flow -> flow
                .channel("inputChannel")
                .split(Order.class, Order::getItems)
                .channel(c -> c.executor(Executors.newCachedThreadPool()))  // process in parallel
                .channel("processLineItemChannel");
    }

    @Bean
    public IntegrationFlow processLineItemFlow() {
        return flow -> flow
                .channel("processLineItemChannel")
                .<LineItem, LineItem>transform(lineItem -> {
                    // process lineItem, e.g., apply discount, calculate tax, etc.
                    lineItem.setPrice(lineItem.getPrice() * 0.95);
                    return lineItem; // return the processed line item
                })
                .channel("aggregationChannel");
    }

    @Bean
    public IntegrationFlow aggregateLineItemsFlow() {
        return flow -> flow
                .channel("aggregationChannel")
                .aggregate(aggregatorSpec -> aggregatorSpec
                        .correlationStrategy(message ->
                                ((LineItem) message.getPayload()).getOrderid())  // Assuming LineItem has getOrderID()
                        .releaseStrategy(group -> group.size() == 10)     // lets assume each order has 10 items
                        .groupTimeout(1000)
                        .discardChannel("outputChannel")  // where to send messages if the release strategy is not met within the timeout
                        .sendPartialResultOnExpiry(true)
                        .outputProcessor(group -> {
                            List<LineItem> processedItems = new ArrayList<>();
                            for (Message<?> message : group.getMessages()) {
                                processedItems.add((LineItem) message.getPayload());
                            }
                            int orderId = processedItems.get(0).getOrderid();  // Assuming LineItem has getOrderID()
                            return new GenericMessage<>(new Order(orderId, processedItems));
                        }))
                .channel("outputChannel");
    }

    @Bean
    public IntegrationFlow outputFlow() {
        return flow -> flow
                .channel("outputChannel")
                .handle(message -> {
                    System.out.println(message.getPayload());
                });
    }
}
