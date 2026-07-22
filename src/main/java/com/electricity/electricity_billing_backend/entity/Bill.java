package com.electricity.electricity_billing_backend.entity;

import com.electricity.electricity_billing_backend.enums.BillStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "bills",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "reading_id")
        }
)
public class Bill extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_id", nullable = false, unique = true)
    private MeterReading meterReading;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal energyCharge;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal fixedCharge;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal fuelSurcharge;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal electricityDuty;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal lateFee = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;


    @Column(nullable = false)
    private LocalDate billDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;


}