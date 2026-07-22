package com.electricity.electricity_billing_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TariffSlabRequest {

    @NotNull
    private Integer fromUnit;

    @NotNull
    private Integer toUnit;

    @NotNull
    private Double ratePerUnit;
}