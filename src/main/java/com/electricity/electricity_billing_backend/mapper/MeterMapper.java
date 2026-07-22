package com.electricity.electricity_billing_backend.mapper;

import com.electricity.electricity_billing_backend.dto.response.MeterResponse;
import com.electricity.electricity_billing_backend.entity.Meter;
import org.springframework.stereotype.Component;

@Component
public class MeterMapper {

    public MeterResponse toResponse(Meter meter) {

        return MeterResponse.builder()
                .id(meter.getId())
                .meterNumber(meter.getMeterNumber())
                .manufacturer(meter.getManufacturer())
                .model(meter.getModel())
                .installationYear(meter.getInstallationYear())
                .status(meter.getStatus())
                .currentReading(meter.getCurrentReading())
                .consumerId(meter.getConsumer().getId())
                .consumerName(meter.getConsumer().getFullName())
                .tariffId(meter.getTariff().getId())
                .tariffName(meter.getTariff().getTariffName())
                .active(meter.getActive())
                .build();
    }
}