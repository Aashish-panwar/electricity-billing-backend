package com.electricity.electricity_billing_backend.dto.response;

import com.electricity.electricity_billing_backend.enums.ReadingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MeterReadingResponse {

    private Long id;

    private Long meterId;

    private String meterNumber;

    private Double previousReading;

    private Double currentReading;

    private Double unitsConsumed;

    private Integer billingMonth;

    private Integer billingYear;

    private LocalDate readingDate;

    private ReadingStatus status;

    private String remarks;

    private Boolean active;
}