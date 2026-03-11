package com.demo.orderservice.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private final BigDecimal totalAmount;

    public Order(String id,
                 String customerId,
                 List<OrderItem> items,
                 BigDecimal totalAmount) {

        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
