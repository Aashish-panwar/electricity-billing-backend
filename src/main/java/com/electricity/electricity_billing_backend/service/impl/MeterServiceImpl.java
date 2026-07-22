package com.electricity.electricity_billing_backend.service.impl;
import org.springframework.cache.annotation.CacheEvict;
import com.electricity.electricity_billing_backend.dto.request.MeterRequest;
import com.electricity.electricity_billing_backend.dto.response.MeterResponse;
import com.electricity.electricity_billing_backend.entity.Consumer;
import com.electricity.electricity_billing_backend.entity.Meter;
import com.electricity.electricity_billing_backend.entity.Tariff;
import com.electricity.electricity_billing_backend.exception.DuplicateResourceException;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.mapper.MeterMapper;
import com.electricity.electricity_billing_backend.repository.ConsumerRepository;
import com.electricity.electricity_billing_backend.repository.MeterRepository;
import com.electricity.electricity_billing_backend.repository.TariffRepository;
import com.electricity.electricity_billing_backend.service.MeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@jakarta.transaction.Transactional
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;
    private final ConsumerRepository consumerRepository;
    private final MeterMapper meterMapper;

    private final TariffRepository tariffRepository;
    @Override
    @CacheEvict(value = "statistics", allEntries = true)
    public MeterResponse createMeter(MeterRequest request) {

        if (meterRepository.existsByMeterNumber(request.getMeterNumber())) {
            throw new DuplicateResourceException("Meter number already exists.");
        }

        Consumer consumer = consumerRepository.findById(request.getConsumerId())
                .orElseThrow(() -> new RuntimeException("Consumer not found."));

        Tariff tariff = tariffRepository.findById(request.getTariffId())
                .filter(Tariff::getActive)
                .orElseThrow(() -> new RuntimeException("Tariff not found."));

        Meter meter = Meter.builder()
                .meterNumber(request.getMeterNumber())
                .manufacturer(request.getManufacturer())
                .model(request.getModel())
                .installationYear(request.getInstallationYear())
                .status(request.getStatus())
                .currentReading(
                        request.getCurrentReading() == null ? 0.0 : request.getCurrentReading()
                )
                .consumer(consumer)
                .tariff(tariff)
                .build();

        Meter savedMeter = meterRepository.save(meter);

        log.info("Meter created: {}", savedMeter.getMeterNumber());

        log.info(
                "Meter {} assigned to consumer {}",
                savedMeter.getMeterNumber(),
                savedMeter.getConsumer().getConsumerNumber()
        );

        return meterMapper.toResponse(savedMeter);
    }

    @Override
    @Transactional
    public MeterResponse getMeterById(Long id) {

        Meter meter = meterRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found."));

        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public List<MeterResponse> getAllMeters() {

        return meterRepository.findAll()
                .stream()
                .filter(Meter::getActive)
                .map(meterMapper::toResponse)
                .toList();
    }

    @Override
    public MeterResponse updateMeter(Long id, MeterRequest request) {

        Meter meter = meterRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Meter not found."));

        Tariff tariff = tariffRepository.findById(request.getTariffId())
                .filter(Tariff::getActive)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));

        meter.setTariff(tariff);
        meter.setManufacturer(request.getManufacturer());
        meter.setModel(request.getModel());
        meter.setInstallationYear(request.getInstallationYear());
        meter.setStatus(request.getStatus());
        meter.setCurrentReading(request.getCurrentReading());

        Meter updatedMeter = meterRepository.save(meter);

        log.info("Meter updated: {}", updatedMeter.getMeterNumber());

        return meterMapper.toResponse(updatedMeter);
    }

    @Override
    @CacheEvict(value = "statistics", allEntries = true)
    public void deleteMeter(Long id) {

        Meter meter = meterRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Meter not found."));

        meter.setActive(false);

        meterRepository.save(meter);

        log.warn("Meter deleted: {}", meter.getMeterNumber());
    }
}