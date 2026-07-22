package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Meter;
import com.electricity.electricity_billing_backend.entity.MeterReading;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    @EntityGraph(attributePaths = "meter")
    List<MeterReading> findByMeterIdOrderByBillingYearDescBillingMonthDesc(Long meterId);

    @EntityGraph(attributePaths = "meter")
    Optional<MeterReading> findTopByMeterOrderByBillingYearDescBillingMonthDesc(Meter meter);

    boolean existsByMeterIdAndBillingMonthAndBillingYear(
            Long meterId,
            Integer billingMonth,
            Integer billingYear
    );

    long countByActiveTrue();

    @Query("""
            SELECT COALESCE(SUM(m.unitsConsumed),0)
            FROM MeterReading m
            WHERE m.active = true
            """)
    Double getTotalUnitsConsumed();

    @EntityGraph(attributePaths = "meter")
    Optional<MeterReading> findByIdAndActiveTrue(Long id);

    @EntityGraph(attributePaths = "meter")
    List<MeterReading> findAllByActiveTrue();

    @Query("""
    SELECT COALESCE(SUM(m.unitsConsumed),0)
    FROM MeterReading m
    WHERE m.billingMonth=:month
    AND m.billingYear=:year
    AND m.active=true
    """)
    Double getMonthlyUnits(
            @Param("month") Integer month,
            @Param("year") Integer year
    );
}