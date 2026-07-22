package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    boolean existsByBillId(Long billId);



    // ===========================
    // JOIN FETCH Queries
    // ===========================

    @Query("""
SELECT p
FROM Payment p
JOIN FETCH p.bill b
JOIN FETCH b.consumer
JOIN FETCH b.meter
WHERE p.id = :id
""")
    Optional<Payment> findDetailsById(@Param("id") Long id);

    @Query("""
SELECT p
FROM Payment p
JOIN FETCH p.bill b
JOIN FETCH b.consumer
JOIN FETCH b.meter
WHERE b.id = :billId
""")
    List<Payment> findDetailsByBillId(@Param("billId") Long billId);

    @Query("""
SELECT p
FROM Payment p
JOIN FETCH p.bill b
JOIN FETCH b.consumer c
JOIN FETCH b.meter
WHERE c.id = :consumerId
""")
    List<Payment> findDetailsByConsumerId(@Param("consumerId") Long consumerId);

    @Query("""
SELECT p
FROM Payment p
JOIN FETCH p.bill b
JOIN FETCH b.consumer
JOIN FETCH b.meter
WHERE p.active = true
ORDER BY p.paymentDate DESC
""")
    List<Payment> findRecentPayments(Pageable pageable);


    @Query("""
SELECT p
FROM Payment p
JOIN FETCH p.bill b
JOIN FETCH b.consumer
JOIN FETCH b.meter
WHERE p.transactionId = :transactionId
""")
    Optional<Payment> findDetailsByTransactionId(
            @Param("transactionId") String transactionId
    );



    @Query("""
SELECT p
FROM Payment p
JOIN FETCH p.bill b
JOIN FETCH b.consumer
JOIN FETCH b.meter
WHERE p.active = true
ORDER BY p.paymentDate DESC
""")
    List<Payment> findAllWithDetails();
}