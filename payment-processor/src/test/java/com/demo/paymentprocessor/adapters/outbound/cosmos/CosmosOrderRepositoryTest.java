package com.demo.paymentprocessor.adapters.outbound.cosmos;

import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.demo.paymentprocessor.domain.model.Order;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;

class CosmosOrderRepositoryTest {

    @Test
    void shouldSaveOrderSuccessfully() {

        CosmosAsyncContainer container = mock(CosmosAsyncContainer.class);
        CosmosItemResponse<Order> response =
                mock(CosmosItemResponse.class);

        when(container.createItem(any(Order.class)))
                .thenReturn(Mono.just(response));

        CosmosOrderRepository repository =
                new CosmosOrderRepository(container);

        Order order = new Order(
                "ORD-1",
                "CLIENT-1",
                List.of(),
                BigDecimal.valueOf(100)
        );

        StepVerifier.create(repository.save(order))
                .verifyComplete();

        verify(container, times(1)).createItem(order);
    }
}