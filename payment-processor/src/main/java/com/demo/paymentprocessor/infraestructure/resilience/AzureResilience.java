package com.demo.paymentprocessor.infraestructure.resilience;

import org.slf4j.Logger;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class AzureResilience {

    public static <T> Mono<T> withRetryAndTimeout(
            Mono<T> operation,
            Logger log,
            String operationName
    ) {
        return operation
                .timeout(Duration.ofSeconds(5))
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))
                                .doBeforeRetry(signal ->
                                        log.warn("Retrying {} attempt {}",
                                                operationName,
                                                signal.totalRetries() + 1)
                                )
                );
    }
}
