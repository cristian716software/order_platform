package com.demo.orderservice.config;

import com.azure.storage.queue.QueueAsyncClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureClientConfig {

    @Value("${azure.storage.queue.connection-string}")
    private String queueConnectionString;

    @Value("${azure.storage.queue.queue-name}")
    private String queueName;

    @Bean
    public QueueAsyncClient queueAsyncClient() {
        return new QueueClientBuilder()
                .connectionString(queueConnectionString)
                .queueName(queueName)
                .buildAsyncClient();
    }
}

