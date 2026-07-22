package com.electricity.electricity_billing_backend.dto.response;


import com.electricity.electricity_billing_backend.enums.BillStatus;
import com.electricity.electricity_billing_backend.enums.PaymentMethod;
import com.electricity.electricity_billing_backend.enums.PaymentStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class PaymentResponse {


    private Long id;


    private String transactionId;


    private Long billId;


    private String billNumber;


    private String consumerName;

    private String meterNumber;


    private BillStatus billStatus;


    private BigDecimal amount;


    private PaymentMethod paymentMethod;


    private PaymentStatus status;


    private LocalDateTime paymentDate;


    private String remarks;


}