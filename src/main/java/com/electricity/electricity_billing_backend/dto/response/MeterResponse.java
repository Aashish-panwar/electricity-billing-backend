package com.electricity.electricity_billing_backend.dto.response;

import com.electricity.electricity_billing_backend.enums.MeterStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeterResponse {

    private Long id;

    private String meterNumber;

    private String manufacturer;

    private String model;

    private Integer installationYear;

    private MeterStatus status;

    private Double currentReading;

    private Long consumerId;

    private String consumerName;

    private Long tariffId;

    private String tariffName;


    private Boolean active;
}