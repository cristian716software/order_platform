package com.demo.paymentprocessor.application.usecase;

import com.demo.paymentprocessor.domain.model.Order;
import com.demo.paymentprocessor.ports.output.AuditStoragePort;
import com.demo.paymentprocessor.ports.output.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class ProcessOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final AuditStoragePort auditStorage;
    private static final Logger log = LoggerFactory.getLogger(ProcessOrderUseCase.class);

    public ProcessOrderUseCase(
            OrderRepositoryPort orderRepository,
            AuditStoragePort auditStorage
    ) {
        this.orderRepository = orderRepository;
        this.auditStorage = auditStorage;
    }

    public Mono<Void> process(Order order) {
        log.info("Processing order {}", order.getId());
        return orderRepository.save(order)
                .then(auditStorage.storeAudit(order));
    }

}
