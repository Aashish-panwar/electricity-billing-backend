package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Optional<Tariff> findByTariffName(String tariffName);

    List<Tariff> findByActiveTrue();

    boolean existsByTariffName(String tariffName);

    long countByActiveTrue();

}