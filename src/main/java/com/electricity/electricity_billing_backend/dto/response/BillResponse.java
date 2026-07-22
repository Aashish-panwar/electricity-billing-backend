package com.electricity.electricity_billing_backend.dto.response;

import com.electricity.electricity_billing_backend.enums.BillStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BillResponse {

    private Long id;

    private String billNumber;

    private Long consumerId;

    private String consumerName;

    private Long meterId;

    private String meterNumber;

    private Long meterReadingId;

    private Double unitsConsumed;

    private BigDecimal energyCharge;

    private BigDecimal fixedCharge;

    private BigDecimal fuelSurcharge;

    private BigDecimal electricityDuty;

    private BigDecimal lateFee;

    private BigDecimal totalAmount;

    private LocalDate billDate;

    private LocalDate dueDate;

    private BillStatus status;

    private Boolean active;

}