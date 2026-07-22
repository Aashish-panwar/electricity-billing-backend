package com.electricity.electricity_billing_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerReportResponse {

    private Long consumerId;

    private String consumerName;

    private String consumerNumber;

    private String meterNumber;

    private Integer totalBills;

    private BigDecimal totalBillAmount;

    private BigDecimal totalPaid;

    private BigDecimal pendingAmount;

    private List<BillResponse> bills;

}