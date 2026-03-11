package com.demo.paymentprocessor.adapters.outbound.blob;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.demo.paymentprocessor.domain.model.Order;
import com.demo.paymentprocessor.infraestructure.resilience.AzureResilience;
import com.demo.paymentprocessor.ports.output.AuditStoragePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class BlobAuditStorage implements AuditStoragePort {

    private final BlobContainerAsyncClient containerClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(BlobAuditStorage.class);

    public BlobAuditStorage(BlobContainerAsyncClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public Mono<Void> storeAudit(Order order) {
        try {
            String json = objectMapper.writeValueAsString(order);
            return AzureResilience.withRetryAndTimeout(
                            containerClient
                                    .getBlobAsyncClient(order.getId() + ".json")
                                    .upload(BinaryData.fromString(json)),
                            log,
                            "Blob upload")
                    .doOnSuccess(v ->
                            log.info("Audit stored for order {}", order.getId())
                    )
                    .then();
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private boolean isRetryableError(Throwable throwable) {
        return throwable instanceof RuntimeException;
    }
}
