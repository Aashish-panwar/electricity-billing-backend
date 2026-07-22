package com.electricity.electricity_billing_backend.dto.response;

import com.electricity.electricity_billing_backend.enums.ConsumerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsumerResponse {

    private Long id;

    private String consumerNumber;

    private String fullName;

    private String mobileNumber;

    private String email;

    private String address;

    private String city;

    private String state;

    private String pinCode;

    private ConsumerType consumerType;

    private Long userId;

    private Boolean active;
}