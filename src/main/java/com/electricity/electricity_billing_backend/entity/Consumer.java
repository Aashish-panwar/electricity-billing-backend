package com.electricity.electricity_billing_backend.entity;

import com.electricity.electricity_billing_backend.enums.ConsumerType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "consumers")
public class Consumer extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String consumerNumber;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String mobileNumber;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 6)
    private String pinCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsumerType consumerType;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}