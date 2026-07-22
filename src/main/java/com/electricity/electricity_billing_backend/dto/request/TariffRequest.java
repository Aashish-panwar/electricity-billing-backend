package com.electricity.electricity_billing_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TariffRequest {

    private String tariffName;

    private BigDecimal ratePerUnit;

    private BigDecimal fixedCharge;

    private BigDecimal fuelSurcharge;

    private BigDecimal electricityDuty;

    private String description;


    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}