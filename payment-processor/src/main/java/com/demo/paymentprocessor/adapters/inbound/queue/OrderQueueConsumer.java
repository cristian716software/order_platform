package com.demo.paymentprocessor.adapters.inbound.queue;

import com.azure.storage.queue.QueueAsyncClient;
import com.demo.paymentprocessor.application.usecase.ProcessOrderUseCase;
import com.demo.paymentprocessor.domain.model.Order;
import com.demo.paymentprocessor.infraestructure.tracing.TracingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.MDC;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
public class OrderQueueConsumer {

    private final QueueAsyncClient queueClient;
    private final ProcessOrderUseCase processOrderUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log =
            LoggerFactory.getLogger(OrderQueueConsumer.class);

    public OrderQueueConsumer(
            QueueAsyncClient queueClient,
            ProcessOrderUseCase processOrderUseCase
    ) {
        this.queueClient = queueClient;
        this.processOrderUseCase = processOrderUseCase;
    }

    @Scheduled(fixedDelay = 2000)
    public void pollQueue() {
        queueClient.receiveMessages(5)
                .flatMap(this::processMessage)
                .subscribe();
    }

    private Mono<Void> processMessage(com.azure.storage.queue.models.QueueMessageItem message) {
        return Mono.fromCallable(() -> parseMessage(message.getBody().toString()))
                .flatMap(entry -> {
                    String traceId = entry.getKey();
                    Order order = entry.getValue();

                    Mono<Void> pipeline = Mono.just(order)
                            .flatMap(o -> {
                                log.info("PAYMENT_PROCESSING_STARTED orderId={} customerId={} amount={}",
                                        o.getId(),
                                        o.getCustomerId(),
                                        o.getTotalAmount());
                                return processOrderUseCase.process(o)
                                        .timeout(Duration.ofSeconds(10))
                                        .retryWhen(
                                                Retry.backoff(3, Duration.ofSeconds(2))
                                                        .doBeforeRetry(signal ->
                                                                log.warn("Retry processing order {} attempt {}",
                                                                        order.getId(),
                                                                        signal.totalRetries() + 1)));
                            })
                            .then(queueClient.deleteMessage(
                                    message.getMessageId(),
                                    message.getPopReceipt()
                            ).then())
                            .doOnSuccess(v ->
                                    log.info("PAYMENT_COMPLETED orderId={} messageId={}",
                                            order.getId(),
                                            message.getMessageId())
                            );

                    return TracingUtils.withTraceId(pipeline, traceId);
                })
                .onErrorResume(ex -> {
                    log.error("Error processing queue message {}", message.getMessageId(), ex);
                    return Mono.empty();
                });
    }

    private Map.Entry<String, Order> parseMessage(String body) throws Exception {
        JsonNode node = objectMapper.readTree(body);
        String traceId = node.get("traceId").asText("no-trace");
        Order order = objectMapper.treeToValue(node.get("order"), Order.class);
        return Map.entry(traceId, order);
    }
}