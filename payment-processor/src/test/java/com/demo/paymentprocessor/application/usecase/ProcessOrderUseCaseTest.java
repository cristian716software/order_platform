package com.demo.paymentprocessor.application.usecase;

import com.demo.paymentprocessor.domain.model.Order;
import com.demo.paymentprocessor.domain.model.OrderItem;
import com.demo.paymentprocessor.ports.output.AuditStoragePort;
import com.demo.paymentprocessor.ports.output.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;

class ProcessOrderUseCaseTest {

    @Test
    void shouldPersistOrderAndStoreAudit() {

        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        AuditStoragePort auditStorage = mock(AuditStoragePort.class);

        when(repository.save(any(Order.class))).thenReturn(Mono.empty());
        when(auditStorage.storeAudit(any(Order.class))).thenReturn(Mono.empty());

        ProcessOrderUseCase useCase =
                new ProcessOrderUseCase(repository, auditStorage);

        Order order = new Order(
                "ORD-1",
                "CLIENT-1",
                List.of(new OrderItem("P1", 1)),
                BigDecimal.valueOf(100)
        );

        Mono<Void> result = useCase.process(order);

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).save(order);
        verify(auditStorage, times(1)).storeAudit(order);
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {

        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        AuditStoragePort auditStorage = mock(AuditStoragePort.class);

        when(repository.save(any(Order.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        when(auditStorage.storeAudit(any(Order.class)))
                .thenReturn(Mono.empty());

        ProcessOrderUseCase useCase =
                new ProcessOrderUseCase(repository, auditStorage);

        Order order = new Order(
                "ORD-2",
                "CLIENT-1",
                List.of(),
                BigDecimal.valueOf(50)
        );

        Mono<Void> result = useCase.process(order);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
