package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.entity.Consumer;
import com.electricity.electricity_billing_backend.repository.ConsumerRepository;
import com.electricity.electricity_billing_backend.service.impl.ConsumerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceImplTest {

    @Mock
    private ConsumerRepository consumerRepository;

    @InjectMocks
    private ConsumerServiceImpl consumerService;

    @Test
    void testGetConsumerById() {

        Consumer consumer = new Consumer();
        consumer.setId(1L);
        consumer.setFullName("Rahul Kumar");
        consumer.setActive(true);

        when(consumerRepository.findById(1L))
                .thenReturn(Optional.of(consumer));

        assertNotNull(consumer);
    }
}