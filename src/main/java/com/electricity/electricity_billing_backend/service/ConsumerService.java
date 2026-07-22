package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.ConsumerRequest;
import com.electricity.electricity_billing_backend.dto.response.ConsumerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ConsumerService {

    ConsumerResponse createConsumer(ConsumerRequest request);

    ConsumerResponse getConsumerById(Long id);

    List<ConsumerResponse> getAllConsumers();

    ConsumerResponse updateConsumer(Long id, ConsumerRequest request);

    void deleteConsumer(Long id);

    Page<ConsumerResponse> getAllConsumers(
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<ConsumerResponse> searchConsumers(
            String keyword,
            int page,
            int size
    );
}