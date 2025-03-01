package com.ok.integration.message;

import java.util.List;
import java.util.UUID;

public class Order {
    private final int orderId;
    private final List<LineItem> items;

    public Order(int orderId, List<LineItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public List<LineItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", items=" + items +
                '}';
    }
}
