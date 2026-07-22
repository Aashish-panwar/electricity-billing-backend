package com.electricity.electricity_billing_backend.repository;

import com.electricity.electricity_billing_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    @Query("""
            SELECT n
            FROM Notification n
            JOIN FETCH n.consumer
            WHERE n.consumer.id = :consumerId
            AND n.active = true
            ORDER BY n.createdAt DESC
            """)
    List<Notification> findByConsumerId(
            @Param("consumerId") Long consumerId
    );

    long countByConsumerIdAndIsReadFalse(Long consumerId);
    List<Notification> findByConsumerIdAndIsReadFalseOrderByCreatedAtDesc(
            Long consumerId
    );
}