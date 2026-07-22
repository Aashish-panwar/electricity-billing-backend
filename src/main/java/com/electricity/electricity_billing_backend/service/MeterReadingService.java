package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.MeterReadingRequest;
import com.electricity.electricity_billing_backend.dto.response.MeterReadingResponse;

import java.util.List;

public interface MeterReadingService {

    MeterReadingResponse addReading(MeterReadingRequest request);

    MeterReadingResponse getReadingById(Long id);

    List<MeterReadingResponse> getAllReadings();

    List<MeterReadingResponse> getReadingsByMeter(Long meterId);

    MeterReadingResponse updateReading(
            Long id,
            MeterReadingRequest request
    );

    void deleteReading(Long id);
}