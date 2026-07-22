package com.electricity.electricity_billing_backend.dto.request;

import com.electricity.electricity_billing_backend.enums.ConsumerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerRequest {

    @NotBlank(message = "Consumer number is required")
    private String consumerNumber;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pin code is required")
    private String pinCode;

    private ConsumerType consumerType;

    private Long userId;
}