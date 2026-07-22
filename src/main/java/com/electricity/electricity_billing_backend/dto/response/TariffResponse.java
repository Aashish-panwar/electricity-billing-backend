package com.electricity.electricity_billing_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TariffResponse {

    private Long id;

    private String tariffName;

    private BigDecimal ratePerUnit;

    private BigDecimal fixedCharge;

    private BigDecimal fuelSurcharge;

    private BigDecimal electricityDuty;

    private String description;

    private Boolean active;


    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}