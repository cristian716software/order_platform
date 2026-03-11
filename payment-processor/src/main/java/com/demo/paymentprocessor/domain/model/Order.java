package com.demo.paymentprocessor.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private String id;
    private String customerId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;

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

