package com.demo.orderservice.application.usecase;

import com.demo.orderservice.domain.model.Order;
import com.demo.orderservice.domain.model.OrderItem;
import com.demo.orderservice.ports.output.OrderQueuePort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    @Test
    void shouldSendOrderToQueueSuccessfully() {

        OrderQueuePort queuePort = mock(OrderQueuePort.class);
        when(queuePort.sendOrder(any(Order.class)))
                .thenReturn(Mono.empty());

        CreateOrderUseCase useCase = new CreateOrderUseCase(queuePort);
        Order order = new Order(
                "ORD-1",
                "CLIENT-1",
                List.of(new OrderItem("P1", 2)),
                BigDecimal.valueOf(100)
        );

        Mono<Void> result = useCase.execute(order);
        StepVerifier.create(result)
                .verifyComplete();

        verify(queuePort, times(1)).sendOrder(order);
    }

    @Test
    void shouldPropagateErrorWhenQueueFails() {

        OrderQueuePort queuePort = mock(OrderQueuePort.class);
        when(queuePort.sendOrder(any(Order.class)))
                .thenReturn(Mono.error(new RuntimeException("Queue failure")));

        CreateOrderUseCase useCase = new CreateOrderUseCase(queuePort);
        Order order = new Order(
                "ORD-1",
                "CLIENT-1",
                List.of(new OrderItem("P1", 2)),
                BigDecimal.valueOf(100)
        );

        Mono<Void> result = useCase.execute(order);
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
