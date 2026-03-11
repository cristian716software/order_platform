package com.demo.orderservice.application.usecase;

import com.demo.orderservice.domain.model.Order;
import com.demo.orderservice.ports.output.OrderQueuePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class CreateOrderUseCase {

    private final OrderQueuePort orderQueuePort;
    private static final Logger log =
            LoggerFactory.getLogger(CreateOrderUseCase.class);

    public CreateOrderUseCase(OrderQueuePort orderQueuePort) {
        this.orderQueuePort = orderQueuePort;
    }

    public Mono<Void> execute(Order order) {
        log.info("Sending order {} to queue", order.getId());
        return orderQueuePort.sendOrder(order);
    }

}
