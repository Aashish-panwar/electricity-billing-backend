package com.electricity.electricity_billing_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest {

    @NotNull(message = "Meter Reading Id is required")
    private Long meterReadingId;

}