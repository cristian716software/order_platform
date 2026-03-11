package com.demo.orderservice.adapters.inbound.rest;

import com.demo.orderservice.adapters.inbound.rest.dto.CreateOrderRequest;
import com.demo.orderservice.adapters.inbound.rest.mapper.OrderMapper;
import com.demo.orderservice.application.usecase.CreateOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class CreateOrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private static final Logger log = LoggerFactory.getLogger(CreateOrderController.class);

    public CreateOrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    public Mono<Void> createOrder(@RequestBody CreateOrderRequest request) {
        return Mono.just(request)
                .map(OrderMapper::toDomain)
                .doOnNext(order ->
                        log.info("Received create order request {}", order.getId())
                )
                .flatMap(createOrderUseCase::execute);
    }

}
