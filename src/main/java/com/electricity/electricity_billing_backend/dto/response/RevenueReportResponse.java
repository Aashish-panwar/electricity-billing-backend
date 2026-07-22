package com.electricity.electricity_billing_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueReportResponse {

    private Integer month;

    private Integer year;

    private Long totalBills;

    private Long paidBills;

    private Long pendingBills;

    private BigDecimal totalRevenue;

}