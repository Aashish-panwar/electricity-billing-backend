package com.electricity.electricity_billing_backend.dto.request;


import com.electricity.electricity_billing_backend.enums.MeterStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MeterRequest {


    @NotBlank(message = "Meter number is required")
    private String meterNumber;


    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;


    @NotBlank(message = "Model is required")
    private String model;


    @NotNull(message = "Installation year is required")
    private Integer installationYear;


    @NotNull(message = "Meter status is required")
    private MeterStatus status;


    @NotNull(message = "Current reading is required")
    @DecimalMin(
            value = "0.0",
            message = "Current reading cannot be negative"
    )
    private Double currentReading;


    @NotNull(message = "Consumer ID is required")
    private Long consumerId;

    private Long tariffId;

}