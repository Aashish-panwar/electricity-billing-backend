package com.electricity.electricity_billing_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tariffs")
public class Tariff extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String tariffName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerUnit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fixedCharge;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fuelSurcharge;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal electricityDuty;

    @Column(length = 300)
    private String description;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;
}