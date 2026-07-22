package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.TariffSlab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TariffSlabRepository extends JpaRepository<TariffSlab, Long> {

    List<TariffSlab> findByTariffIdOrderByFromUnitAsc(Long tariffId);
}