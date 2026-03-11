package com.demo.orderservice.ports.output;

import com.demo.orderservice.domain.model.Order;
import reactor.core.publisher.Mono;

public interface OrderQueuePort {

    Mono<Void> sendOrder(Order order);

}