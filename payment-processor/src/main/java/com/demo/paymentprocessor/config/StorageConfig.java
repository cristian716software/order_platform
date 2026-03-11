package com.demo.paymentprocessor.config;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.demo.paymentprocessor.adapters.outbound.blob.BlobAuditStorage;
import com.demo.paymentprocessor.ports.output.AuditStoragePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public AuditStoragePort auditStoragePort(
            BlobContainerAsyncClient containerClient) {

        return new BlobAuditStorage(containerClient);
    }

}
