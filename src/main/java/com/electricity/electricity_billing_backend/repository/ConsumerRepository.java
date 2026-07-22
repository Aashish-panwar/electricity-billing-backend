package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    Optional<Consumer> findByConsumerNumber(String consumerNumber);

    boolean existsByConsumerNumber(String consumerNumber);

    boolean existsByEmail(String email);

    Optional<Consumer> findByIdAndActiveTrue(Long id);
    long countByActiveTrue();

    Page<Consumer> findByActiveTrue(Pageable pageable);

    Page<Consumer> findByActiveTrueAndFullNameContainingIgnoreCaseOrConsumerNumberContainingIgnoreCase(
            String fullName,
            String consumerNumber,
            Pageable pageable
    );

    @Query("""
SELECT c
FROM Consumer c
WHERE c.active=true
AND (
LOWER(c.fullName) LIKE LOWER(CONCAT('%',:keyword,'%'))
OR LOWER(c.consumerNumber) LIKE LOWER(CONCAT('%',:keyword,'%'))
OR LOWER(c.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
OR c.mobileNumber LIKE CONCAT('%',:keyword,'%')
)
""")
    Page<Consumer> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}