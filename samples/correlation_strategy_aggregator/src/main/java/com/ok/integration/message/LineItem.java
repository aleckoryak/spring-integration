package com.ok.integration.message;

public class LineItem {
    private final int orderid;
    private final String name;
    private double price;
    private final int quantity;


    public LineItem(String name, double price, int quantity, int orderid) {
        this.orderid = orderid;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "orderid=" + orderid +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
