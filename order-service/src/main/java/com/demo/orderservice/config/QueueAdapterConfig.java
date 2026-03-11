package com.demo.orderservice.config;

import com.azure.storage.queue.QueueAsyncClient;
import com.demo.orderservice.adapters.inbound.queue.AzureQueueAdapter;
import com.demo.orderservice.ports.output.OrderQueuePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.tracing.Tracer;

@Configuration
public class QueueAdapterConfig {

    @Bean
    public OrderQueuePort orderQueuePort(
            QueueAsyncClient queueAsyncClient,
            Tracer tracer
    ) {
        return new AzureQueueAdapter(queueAsyncClient, tracer);
    }
}
