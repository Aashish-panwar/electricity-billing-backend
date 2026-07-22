package com.electricity.electricity_billing_backend.entity;

import com.electricity.electricity_billing_backend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tariff_slabs")
public class TariffSlab extends BaseEntity {

    @Column(nullable = false)
    private Integer fromUnit;

    @Column(nullable = false)
    private Integer toUnit;

    @Column(nullable = false)
    private Double ratePerUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
}