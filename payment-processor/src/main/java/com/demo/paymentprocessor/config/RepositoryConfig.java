package com.demo.paymentprocessor.config;

import com.azure.cosmos.CosmosAsyncContainer;
import com.demo.paymentprocessor.adapters.outbound.cosmos.CosmosOrderRepository;
import com.demo.paymentprocessor.ports.output.OrderRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public OrderRepositoryPort orderRepositoryPort(
            CosmosAsyncContainer container) {

        return new CosmosOrderRepository(container);
    }
}
