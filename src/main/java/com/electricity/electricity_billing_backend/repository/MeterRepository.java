package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Meter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeterRepository extends JpaRepository<Meter, Long> {

    Optional<Meter> findByMeterNumber(String meterNumber);

    boolean existsByMeterNumber(String meterNumber);

    Optional<Meter> findByIdAndActiveTrue(Long id);
    long countByActiveTrue();
    List<Meter> findByConsumerId(Long consumerId);
}