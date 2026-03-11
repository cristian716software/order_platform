package com.demo.paymentprocessor.adapters.outbound.cosmos;

import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosException;
import com.demo.paymentprocessor.domain.model.Order;
import com.demo.paymentprocessor.infraestructure.resilience.AzureResilience;
import com.demo.paymentprocessor.ports.output.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class CosmosOrderRepository implements OrderRepositoryPort {

    private final CosmosAsyncContainer container;
    private static final Logger log = LoggerFactory.getLogger(CosmosOrderRepository.class);

    public CosmosOrderRepository(CosmosAsyncContainer container) {
        this.container = container;
    }

    @Override
    public Mono<Void> save(Order order) {
        return AzureResilience.withRetryAndTimeout(
                        container.createItem(order),
                        log,
                        "CosmosDB save"
                )
                .onErrorResume(CosmosException.class, ex -> {
                    if (ex.getStatusCode() == 409) {
                        log.warn("Duplicate order ignored {}", order.getId());
                        return Mono.empty();}
                    log.error("Failed to persist order {}", order.getId(), ex);
                    return Mono.error(ex);
                })
                .doOnSuccess(response ->
                        log.info("Order {} persisted successfully", order.getId())
                )
                .then();
    }
}
