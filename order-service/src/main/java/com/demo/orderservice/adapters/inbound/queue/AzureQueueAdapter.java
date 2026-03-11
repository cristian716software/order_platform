package com.demo.orderservice.adapters.inbound.queue;

import com.azure.storage.queue.QueueAsyncClient;
import com.demo.orderservice.domain.model.Order;
import com.demo.orderservice.ports.output.OrderQueuePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import io.micrometer.tracing.Tracer;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

public class AzureQueueAdapter implements OrderQueuePort {

    private final QueueAsyncClient queueAsyncClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Tracer tracer;

    private static final Logger log =
            LoggerFactory.getLogger(AzureQueueAdapter.class);

    public AzureQueueAdapter(QueueAsyncClient queueAsyncClient, Tracer tracer) {
        this.queueAsyncClient = queueAsyncClient;
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> sendOrder(Order order) {
        return Mono.deferContextual(contextView -> {
            String traceId = tracer.currentSpan() != null
                    ? tracer.currentSpan().context().traceId()
                    : contextView.getOrDefault("traceId", "no-trace");

            try {
                Map<String, Object> payload = Map.of(
                        "traceId", traceId,
                        "order", order
                );
                String message = objectMapper.writeValueAsString(payload);
                log.info("Publishing order {} with traceId {}", order.getId(), traceId);

                return queueAsyncClient
                        .sendMessage(message)
                        .timeout(Duration.ofSeconds(5))
                        .retryWhen(
                                Retry.backoff(3, Duration.ofSeconds(2))
                                        .filter(this::isRetryableError)
                                        .doBeforeRetry(signal ->
                                                log.warn("Retrying queue send attempt {}",
                                                        signal.totalRetries() + 1)
                                        )
                        )
                        .doOnSuccess(r -> log.info("Order {} sent to queue successfully", order.getId()))
                        .then();
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    private boolean isRetryableError(Throwable throwable) {
        return throwable instanceof RuntimeException;
    }
}