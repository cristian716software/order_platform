package com.demo.paymentprocessor.infraestructure.tracing;

import org.slf4j.MDC;
import reactor.core.publisher.Mono;

public class TracingUtils {

    private static final String TRACE_ID_KEY = "traceId";

    private TracingUtils() {}

    public static <T> Mono<T> withTraceId(Mono<T> mono, String traceId) {
        return Mono.fromRunnable(() -> MDC.put(TRACE_ID_KEY, traceId))
                .then(mono)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        MDC.put(TRACE_ID_KEY, traceId);
                    }
                })
                .doFinally(signal -> MDC.remove(TRACE_ID_KEY));
    }
}