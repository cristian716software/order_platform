package com.demo.orderservice.domain.model;

public class OrderItem {

    private final String productId;
    private final Integer quantity;

    public OrderItem(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
