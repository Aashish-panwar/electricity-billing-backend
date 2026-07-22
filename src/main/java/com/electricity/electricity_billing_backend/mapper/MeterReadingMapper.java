package com.electricity.electricity_billing_backend.mapper;

import com.electricity.electricity_billing_backend.dto.response.MeterReadingResponse;
import com.electricity.electricity_billing_backend.entity.Meter;
import com.electricity.electricity_billing_backend.entity.MeterReading;
import org.springframework.stereotype.Component;

@Component
public class MeterReadingMapper {

    public MeterReadingResponse toResponse(MeterReading reading) {

        Meter meter = reading.getMeter();

        return MeterReadingResponse.builder()
                .id(reading.getId())
                .meterId(meter != null ? meter.getId() : null)
                .meterNumber(meter != null ? meter.getMeterNumber() : null)
                .previousReading(reading.getPreviousReading())
                .currentReading(reading.getCurrentReading())
                .unitsConsumed(reading.getUnitsConsumed())
                .billingMonth(reading.getBillingMonth())
                .billingYear(reading.getBillingYear())
                .readingDate(reading.getReadingDate())
                .status(reading.getStatus())
                .remarks(reading.getRemarks())
                .active(reading.getActive())
                .build();
    }
}