package com.electricity.electricity_billing_backend.entity;


import com.electricity.electricity_billing_backend.enums.PaymentMethod;
import com.electricity.electricity_billing_backend.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {


    @Column(nullable = false, unique = true)
    private String transactionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;


    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;


    @Column(nullable = false)
    private LocalDateTime paymentDate;


    private String remarks;

}