package com.demo.orderservice.adapters.inbound.rest.dto;

public class CreateOrderItemRequest {

    private String productId;
    private Integer quantity;

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
