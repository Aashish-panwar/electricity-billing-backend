package com.electricity.electricity_billing_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalConsumers;

    private Long totalMeters;

    private Long totalBills;

    private BigDecimal totalRevenue;

    private Long paidBills;

    private Long pendingBills;

    private Long overdueBills;

    private Long activeMeters;

}