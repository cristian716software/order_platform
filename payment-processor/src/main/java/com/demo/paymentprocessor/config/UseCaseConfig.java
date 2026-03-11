package com.demo.paymentprocessor.config;

import com.demo.paymentprocessor.application.usecase.ProcessOrderUseCase;
import com.demo.paymentprocessor.ports.output.AuditStoragePort;
import com.demo.paymentprocessor.ports.output.OrderRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessOrderUseCase processOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            AuditStoragePort auditStoragePort
    ) {
        return new ProcessOrderUseCase(
                orderRepositoryPort,
                auditStoragePort
        );
    }
}
