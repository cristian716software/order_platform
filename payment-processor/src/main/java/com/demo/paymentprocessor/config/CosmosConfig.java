package com.demo.paymentprocessor.config;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosConfig {

    @Value("${azure.cosmos.endpoint}")
    private String ENDPOINT;

    @Value("${azure.cosmos.key}")
    private String KEY;

    @Value("${azure.cosmos.database}")
    private String DATABASE;

    @Value("${azure.cosmos.container}")
    private String CONTAINER;

    @Bean
    public CosmosAsyncClient cosmosAsyncClient() {
        return new CosmosClientBuilder()
                .endpoint(ENDPOINT)
                .key(KEY)
                .gatewayMode()
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .contentResponseOnWriteEnabled(true)
                .buildAsyncClient();
    }

    @Bean
    public CosmosAsyncContainer orderContainer(CosmosAsyncClient client) {
        return client
                .getDatabase(DATABASE)
                .getContainer(CONTAINER);
    }
}
