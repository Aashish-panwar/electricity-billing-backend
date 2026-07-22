package com.electricity.electricity_billing_backend.dto.request;


import com.electricity.electricity_billing_backend.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class PaymentRequest {


    @NotNull(message = "Bill ID is required")
    private Long billId;


    @NotNull(message = "Amount is required")
    private BigDecimal amount;


    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;


    private String remarks;

}