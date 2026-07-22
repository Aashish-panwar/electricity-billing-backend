package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.dto.request.MeterReadingRequest;
import com.electricity.electricity_billing_backend.dto.response.MeterReadingResponse;
import com.electricity.electricity_billing_backend.entity.Meter;
import com.electricity.electricity_billing_backend.entity.MeterReading;
import com.electricity.electricity_billing_backend.enums.ReadingStatus;
import com.electricity.electricity_billing_backend.exception.BadRequestException;
import com.electricity.electricity_billing_backend.mapper.MeterReadingMapper;
import com.electricity.electricity_billing_backend.repository.MeterReadingRepository;
import com.electricity.electricity_billing_backend.repository.MeterRepository;
import com.electricity.electricity_billing_backend.service.MeterReadingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterRepository meterRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final MeterReadingMapper meterReadingMapper;

    @Override
    public MeterReadingResponse addReading(MeterReadingRequest request) {

        Meter meter = meterRepository.findByIdAndActiveTrue(request.getMeterId())
                .orElseThrow(() -> new RuntimeException("Meter not found."));

        if (meterReadingRepository.existsByMeterIdAndBillingMonthAndBillingYear(
                meter.getId(),
                request.getBillingMonth(),
                request.getBillingYear())) {

            throw new RuntimeException("Reading already exists for this billing month.");
        }

        double previousReading = meter.getCurrentReading();

        if (request.getCurrentReading() < previousReading) {
            throw new BadRequestException("Current reading must be greater.");
        }

        double unitsConsumed = request.getCurrentReading() - previousReading;

        MeterReading reading = MeterReading.builder()
                .meter(meter)
                .previousReading(previousReading)
                .currentReading(request.getCurrentReading())
                .unitsConsumed(unitsConsumed)
                .billingMonth(request.getBillingMonth())
                .billingYear(request.getBillingYear())
                .readingDate(request.getReadingDate())
                .status(ReadingStatus.VERIFIED)
                .remarks(request.getRemarks())
                .build();

        meter.setCurrentReading(request.getCurrentReading());

        meterRepository.save(meter);

        MeterReading saved = meterReadingRepository.save(reading);

        return meterReadingMapper.toResponse(saved);
    }

    @Override
    public MeterReadingResponse getReadingById(Long id) {

        MeterReading reading = meterReadingRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Reading not found."));

        return meterReadingMapper.toResponse(reading);
    }

    @Override
    public List<MeterReadingResponse> getAllReadings() {

        return meterReadingRepository.findAllByActiveTrue()
                .stream()
                .map(meterReadingMapper::toResponse)
                .toList();
    }

    @Override
    public List<MeterReadingResponse> getReadingsByMeter(Long meterId) {

        return meterReadingRepository
                .findByMeterIdOrderByBillingYearDescBillingMonthDesc(meterId)
                .stream()
                .map(meterReadingMapper::toResponse)
                .toList();
    }

    @Override
    public MeterReadingResponse updateReading(
            Long id,
            MeterReadingRequest request) {

        MeterReading reading = meterReadingRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Meter reading not found."));

        if (request.getCurrentReading() < reading.getPreviousReading()) {
            throw new RuntimeException("Current reading cannot be less than previous reading.");
        }

        double unitsConsumed =
                request.getCurrentReading() - reading.getPreviousReading();

        reading.setCurrentReading(request.getCurrentReading());
        reading.setUnitsConsumed(unitsConsumed);
        reading.setBillingMonth(request.getBillingMonth());
        reading.setBillingYear(request.getBillingYear());
        reading.setReadingDate(request.getReadingDate());
        reading.setRemarks(request.getRemarks());

        Meter meter = reading.getMeter();
        meter.setCurrentReading(request.getCurrentReading());

        meterRepository.save(meter);

        return meterReadingMapper.toResponse(
                meterReadingRepository.save(reading)
        );
    }

    @Override
    public void deleteReading(Long id) {

        MeterReading reading = meterReadingRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Reading not found."));

        reading.setActive(false);

        meterReadingRepository.save(reading);
    }
}