package com.electricity.electricity_billing_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReportResponse {

    private String transactionId;

    private String consumerName;

    private String billNumber;

    private BigDecimal amount;

    private String paymentMethod;

    private LocalDateTime paymentDate;

}