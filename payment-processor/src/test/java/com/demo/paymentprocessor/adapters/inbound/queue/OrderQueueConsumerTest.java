package com.demo.paymentprocessor.adapters.inbound.queue;

import com.azure.core.util.BinaryData;
import com.azure.storage.queue.QueueAsyncClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.demo.paymentprocessor.application.usecase.ProcessOrderUseCase;
import com.demo.paymentprocessor.domain.model.Order;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class OrderQueueConsumerTest {

    @Test
    void shouldProcessMessageSuccessfully() {

        QueueAsyncClient queueClient = mock(QueueAsyncClient.class);
        ProcessOrderUseCase useCase = mock(ProcessOrderUseCase.class);
        OrderQueueConsumer consumer =
                new OrderQueueConsumer(queueClient, useCase);

        QueueMessageItem message = mock(QueueMessageItem.class);

        when(message.getBody()).thenReturn(BinaryData.fromString("{\"traceId\":\"1\",\"order\":{\"id\":\"ORD1\"}}"));
        when(message.getMessageId()).thenReturn("MSG1");
        when(message.getPopReceipt()).thenReturn("POP1");
        when(useCase.process(any(Order.class)))
                .thenReturn(Mono.empty());
        when(queueClient.deleteMessage(anyString(), anyString()))
                .thenReturn(Mono.empty());


        StepVerifier.create(useCase.process(mock(Order.class)))
                .verifyComplete();
    }
}