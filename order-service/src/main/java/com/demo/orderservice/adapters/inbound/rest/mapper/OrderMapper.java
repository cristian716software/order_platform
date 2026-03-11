package com.demo.orderservice.adapters.inbound.rest.mapper;

import com.demo.orderservice.adapters.inbound.rest.dto.CreateOrderRequest;
import com.demo.orderservice.domain.model.Order;
import com.demo.orderservice.domain.model.OrderItem;

import java.util.List;

public class OrderMapper {

    public static Order toDomain(CreateOrderRequest request) {

        List<OrderItem> items = request.getItems()
                .stream()
                .map(i -> new OrderItem(
                        i.getProductId(),
                        i.getQuantity()))
                .toList();

        return new Order(
                request.getId(),
                request.getCustomerId(),
                items,
                request.getTotalAmount()
        );
    }

}
