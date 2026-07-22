package com.electricity.electricity_billing_backend.service.impl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.electricity.electricity_billing_backend.dto.request.TariffRequest;
import com.electricity.electricity_billing_backend.dto.response.TariffResponse;
import com.electricity.electricity_billing_backend.entity.Tariff;
import com.electricity.electricity_billing_backend.exception.DuplicateResourceException;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.mapper.TariffMapper;
import com.electricity.electricity_billing_backend.repository.TariffRepository;
import com.electricity.electricity_billing_backend.service.TariffService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;

    @Override
    @CacheEvict(value = "tariffs", allEntries = true)
    public TariffResponse createTariff(TariffRequest request) {

        if (tariffRepository.existsByTariffName(request.getTariffName())) {
            throw new DuplicateResourceException("Tariff already exists.");
        }

        Tariff tariff = tariffMapper.toEntity(request);

        // Default values
        if (tariff.getEffectiveFrom() == null) {
            tariff.setEffectiveFrom(LocalDate.now());
        }

        if (tariff.getEffectiveTo() == null) {
            tariff.setEffectiveTo(LocalDate.of(2099, 12, 31));
        }

        Tariff saved = tariffRepository.save(tariff);

        return tariffMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "tariffs", allEntries = true)
    public TariffResponse updateTariff(Long id, TariffRequest request) {

        Tariff tariff = tariffRepository.findById(id)
                .filter(Tariff::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff not found."));

        tariffMapper.updateEntity(tariff, request);

        if (tariff.getEffectiveFrom() == null) {
            tariff.setEffectiveFrom(LocalDate.now());
        }

        if (tariff.getEffectiveTo() == null) {
            tariff.setEffectiveTo(LocalDate.of(2099, 12, 31));
        }

        Tariff updated = tariffRepository.save(tariff);

        return tariffMapper.toResponse(updated);
    }

    @Override
    public TariffResponse getTariffById(Long id) {

        Tariff tariff = tariffRepository.findById(id)
                .filter(Tariff::getActive)
                .orElseThrow(() -> new RuntimeException("Tariff not found."));

        return tariffMapper.toResponse(tariff);
    }

    @Cacheable(value = "tariffs")
    @Override
    public List<TariffResponse> getAllTariffs() {

        log.info("Loading Tariffs from Database...");

        return tariffRepository.findByActiveTrue()
                .stream()
                .map(tariffMapper::toResponse)
                .toList();
    }
    @Override
    @CacheEvict(value = "tariffs", allEntries = true)
    public void deleteTariff(Long id) {

        Tariff tariff = tariffRepository.findById(id)
                .filter(Tariff::getActive)
                .orElseThrow(() -> new RuntimeException("Tariff not found."));

        tariff.setActive(false);

        tariffRepository.save(tariff);
    }
}