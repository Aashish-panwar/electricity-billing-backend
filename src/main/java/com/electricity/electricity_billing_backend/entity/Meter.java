package com.electricity.electricity_billing_backend.entity;


import com.electricity.electricity_billing_backend.enums.MeterStatus;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "meters")
public class Meter extends BaseEntity {


    @Column(nullable = false, unique = true, length = 30)
    private String meterNumber;


    @Column(nullable = false)
    private String manufacturer;


    @Column(nullable = false)
    private String model;


    @Column(nullable = false)
    private Integer installationYear;



    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterStatus status = MeterStatus.ACTIVE;



    @Builder.Default
    @Column(nullable = false)
    private Double currentReading = 0.0;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "consumer_id",
            nullable = false
    )
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;


}