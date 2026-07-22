package com.electricity.electricity_billing_backend.mapper;

import com.electricity.electricity_billing_backend.dto.response.ConsumerResponse;
import com.electricity.electricity_billing_backend.entity.Consumer;
import org.springframework.stereotype.Component;

@Component
public class ConsumerMapper {

    public ConsumerResponse toResponse(Consumer consumer) {

        return ConsumerResponse.builder()
                .id(consumer.getId())
                .consumerNumber(consumer.getConsumerNumber())
                .fullName(consumer.getFullName())
                .mobileNumber(consumer.getMobileNumber())
                .email(consumer.getEmail())
                .address(consumer.getAddress())
                .city(consumer.getCity())
                .state(consumer.getState())
                .pinCode(consumer.getPinCode())
                .consumerType(consumer.getConsumerType())
                .userId(consumer.getUser().getId())
                .active(consumer.getActive())
                .build();
    }
}