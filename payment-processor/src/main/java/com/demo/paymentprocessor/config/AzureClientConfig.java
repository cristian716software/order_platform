package com.demo.paymentprocessor.config;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.queue.QueueAsyncClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureClientConfig {

    @Value("${azure.storage.queue.connection-string}")
    private String queueConnectionString;

    @Value("${azure.storage.blob.connection-string}")
    private String blobConnectionString;

    @Value("${azure.storage.queue.queue-name}")
    private String queueName;

    @Value("${azure.storage.blob.container-name}")
    private String blobContainer;

    @Bean
    public QueueAsyncClient queueAsyncClient() {
        return new QueueClientBuilder()
                .connectionString(queueConnectionString)
                .queueName(queueName)
                .buildAsyncClient();
    }

    @Bean
    public BlobContainerAsyncClient blobContainerAsyncClient() {
        return new BlobContainerClientBuilder()
                .connectionString(blobConnectionString)
                .containerName(blobContainer)
                .buildAsyncClient();
    }

}
