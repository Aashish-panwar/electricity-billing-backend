package com.electricity.electricity_billing_backend.entity;

import com.electricity.electricity_billing_backend.enums.ReadingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "meter_readings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_meter_month_year",
                        columnNames = {"meter_id", "billingMonth", "billingYear"}
                )
        }
)
public class MeterReading extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @Column(nullable = false)
    private Double previousReading;

    @Column(nullable = false)
    private Double currentReading;

    @Column(nullable = false)
    private Double unitsConsumed;

    @Column(nullable = false)
    private Integer billingMonth;

    @Column(nullable = false)
    private Integer billingYear;

    @Column(nullable =false)
    private LocalDate readingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status;

    @Column(length = 300)
    private String remarks;
}