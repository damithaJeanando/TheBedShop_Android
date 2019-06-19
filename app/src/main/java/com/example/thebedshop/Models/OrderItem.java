package com.example.thebedshop.Models;

public class OrderItem {
    private int orderItemId;

    private Product product;

    private int amount;

    private CustomOrders customOrders;

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CustomOrders getCustomOrders() {
        return customOrders;
    }

    public void setCustomOrders(CustomOrders customOrders) {
        this.customOrders = customOrders;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
