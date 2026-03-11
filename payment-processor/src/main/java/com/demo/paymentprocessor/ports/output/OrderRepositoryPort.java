package com.demo.paymentprocessor.ports.output;

import com.demo.paymentprocessor.domain.model.Order;
import reactor.core.publisher.Mono;

public interface OrderRepositoryPort {

    Mono<Void> save(Order order);

}
