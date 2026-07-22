package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.MeterRequest;
import com.electricity.electricity_billing_backend.dto.response.MeterResponse;

import java.util.List;

public interface MeterService {

    MeterResponse createMeter(MeterRequest request);

    MeterResponse getMeterById(Long id);

    List<MeterResponse> getAllMeters();

    MeterResponse updateMeter(Long id, MeterRequest request);

    void deleteMeter(Long id);
}