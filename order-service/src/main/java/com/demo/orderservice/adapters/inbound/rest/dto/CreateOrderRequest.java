package com.demo.orderservice.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {

    private String id;
    private String customerId;
    private List<CreateOrderItemRequest> items;
    private BigDecimal totalAmount;

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<CreateOrderItemRequest> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
