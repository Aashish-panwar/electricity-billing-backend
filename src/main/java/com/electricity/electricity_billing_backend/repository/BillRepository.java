package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    // =========================
    // Find Methods
    // =========================

    @Query("""
            SELECT b
            FROM Bill b
            JOIN FETCH b.consumer
            JOIN FETCH b.meter
            JOIN FETCH b.meterReading
            WHERE b.billNumber = :billNumber
            """)
    Optional<Bill> findByBillNumber(@Param("billNumber") String billNumber);


    @Query("""
            SELECT b
            FROM Bill b
            JOIN FETCH b.consumer
            JOIN FETCH b.meter
            JOIN FETCH b.meterReading
            WHERE b.consumer.id = :consumerId
            AND b.active = true
            ORDER BY b.billDate DESC
            """)
    List<Bill> findByConsumerId(@Param("consumerId") Long consumerId);


    @Query("""
            SELECT b
            FROM Bill b
            JOIN FETCH b.consumer
            JOIN FETCH b.meter
            JOIN FETCH b.meterReading
            WHERE b.meter.id = :meterId
            """)
    List<Bill> findByMeterId(@Param("meterId") Long meterId);


    @Query("""
            SELECT b
            FROM Bill b
            JOIN FETCH b.consumer
            JOIN FETCH b.meter
            JOIN FETCH b.meterReading
            WHERE b.status = :status
            AND b.active = true
            """)
    List<Bill> findByStatus(@Param("status") BillStatus status);


    @Query("""
            SELECT b
            FROM Bill b
            JOIN FETCH b.consumer
            JOIN FETCH b.meter
            JOIN FETCH b.meterReading
            WHERE b.consumer.id = :consumerId
            AND b.status = :status
            AND b.active = true
            """)
    List<Bill> findByConsumerIdAndStatus(
            @Param("consumerId") Long consumerId,
            @Param("status") BillStatus status
    );


    Optional<Bill> findTopByMeterIdOrderByBillDateDesc(Long meterId);

    List<Bill> findByDueDateBeforeAndStatus(
            LocalDate date,
            BillStatus status
    );


    @Query("""
            SELECT b
            FROM Bill b
            JOIN FETCH b.consumer
            JOIN FETCH b.meter
            JOIN FETCH b.meterReading
            WHERE b.active = true
            ORDER BY b.billDate DESC
            """)
    List<Bill> findByActiveTrue();


    // =========================
    // Validation
    // =========================

    boolean existsByMeterReadingId(Long meterReadingId);


    // =========================
    // Dashboard Statistics
    // =========================

    long countByActiveTrue();

    long countByStatus(BillStatus status);

    long countByStatusAndActiveTrue(BillStatus status);


    // =========================
    // Revenue
    // =========================

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount),0)
            FROM Bill b
            WHERE b.status='PAID'
            AND b.active=true
            """)
    BigDecimal getTotalRevenue();


    @Query("""
            SELECT COALESCE(SUM(b.totalAmount),0)
            FROM Bill b
            WHERE MONTH(b.billDate)=:month
            AND YEAR(b.billDate)=:year
            AND b.status='PAID'
            AND b.active=true
            """)
    BigDecimal getMonthlyRevenue(
            @Param("month") Integer month,
            @Param("year") Integer year
    );





    @Query("""
        SELECT b
        FROM Bill b
        JOIN FETCH b.consumer
        JOIN FETCH b.meter
        JOIN FETCH b.meterReading
        WHERE b.consumer.id = :consumerId
        AND b.active = true
        ORDER BY b.billDate DESC
        """)
    List<Bill> findDetailsByConsumerId(
            @Param("consumerId") Long consumerId
    );

    @Query("""
SELECT COALESCE(SUM(mr.unitsConsumed),0)
FROM Bill b
JOIN b.meterReading mr
WHERE MONTH(b.billDate)=:month
AND YEAR(b.billDate)=:year
AND b.active=true
""")
    Double getMonthlyUnits(
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("""
SELECT COUNT(b)
FROM Bill b
WHERE MONTH(b.billDate)=:month
AND YEAR(b.billDate)=:year
AND b.active=true
""")
    Long countBillsByMonth(
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("""
SELECT COUNT(b)
FROM Bill b
WHERE MONTH(b.billDate)=:month
AND YEAR(b.billDate)=:year
AND b.status='PAID'
AND b.active=true
""")
    Long countPaidBillsByMonth(
            @Param("month") Integer month,
            @Param("year") Integer year
    );


    @Query("""
SELECT COUNT(b)
FROM Bill b
WHERE MONTH(b.billDate)=:month
AND YEAR(b.billDate)=:year
AND b.status='PENDING'
AND b.active=true
""")
    Long countPendingBillsByMonth(
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("""
SELECT b
FROM Bill b
WHERE b.status='PENDING'
AND b.dueDate < CURRENT_DATE
AND b.active=true
""")
    List<Bill> findOverdueBills();

    @Query("""
SELECT b
FROM Bill b
WHERE b.status = com.electricity.electricity_billing_backend.enums.BillStatus.GENERATED
AND b.dueDate = :dueDate
""")
    List<Bill> findBillsDueTomorrow(@Param("dueDate") LocalDate dueDate);

    @Query("""
SELECT b
FROM Bill b
JOIN FETCH b.consumer
JOIN FETCH b.meter
JOIN FETCH b.meterReading
WHERE b.id = :id
""")
    Optional<Bill> findBillWithDetails(@Param("id") Long id);


    @Query("""
SELECT
MONTHNAME(b.billDate),
COALESCE(SUM(b.totalAmount),0)

FROM Bill b

WHERE b.active = true
AND b.status = 'PAID'

GROUP BY
MONTH(b.billDate),
MONTHNAME(b.billDate)

ORDER BY
MONTH(b.billDate)
""")
    List<Object[]> getMonthlyRevenueChart();
}