package com.electricity.electricity_billing_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRevenueResponse {

    private Integer month;

    private Integer year;

    private BigDecimal revenue;

}