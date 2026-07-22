package com.electricity.electricity_billing_backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MeterReadingRequest {

    @NotNull(message = "Meter ID is required")
    private Long meterId;

    @NotNull(message = "Current reading is required")
    @Min(value = 0, message = "Current reading cannot be negative")
    private Double currentReading;

    @NotNull(message = "Billing month is required")
    @Min(1)
    @Max(12)
    private Integer billingMonth;

    @NotNull(message = "Billing year is required")
    private Integer billingYear;

    @NotNull(message = "Reading date is required")
    private LocalDate readingDate;

    private String remarks;
}