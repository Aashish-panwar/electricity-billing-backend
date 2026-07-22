package com.electricity.electricity_billing_backend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyUnitConsumptionResponse {

    private Integer month;

    private Integer year;

    private Double totalUnits;

}