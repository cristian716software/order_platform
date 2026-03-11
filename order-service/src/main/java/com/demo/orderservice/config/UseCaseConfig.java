package com.demo.orderservice.config;

import com.demo.orderservice.application.usecase.CreateOrderUseCase;
import com.demo.orderservice.ports.output.OrderQueuePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderQueuePort orderQueuePort) {
        return new CreateOrderUseCase(orderQueuePort);
    }
}
