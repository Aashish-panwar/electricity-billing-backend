package com.electricity.electricity_billing_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TariffSlabResponse {

    private Long id;

    private Integer fromUnit;

    private Integer toUnit;

    private Double ratePerUnit;
}