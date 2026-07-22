package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.entity.Consumer;
import com.electricity.electricity_billing_backend.entity.MeterReading;
import com.electricity.electricity_billing_backend.entity.Tariff;
import com.electricity.electricity_billing_backend.enums.BillStatus;
import com.electricity.electricity_billing_backend.service.BillCalculationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BillCalculationServiceImpl implements BillCalculationService {

    @Override
    public Bill calculateBill(MeterReading reading) {

        Consumer consumer = reading.getMeter().getConsumer();

        Tariff tariff = reading.getMeter().getTariff();

        BigDecimal units = BigDecimal.valueOf(reading.getUnitsConsumed());

        // Energy Charge
        BigDecimal energyCharge = units
                .multiply(tariff.getRatePerUnit())
                .setScale(2, RoundingMode.HALF_UP);

        // Fixed Charge
        BigDecimal fixedCharge = tariff.getFixedCharge();

        // Fuel Surcharge
        BigDecimal fuelSurcharge = units
                .multiply(tariff.getFuelSurcharge())
                .setScale(2, RoundingMode.HALF_UP);

        // Electricity Duty (percentage)
        BigDecimal electricityDuty = energyCharge
                .multiply(tariff.getElectricityDuty())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        // Late Fee
        BigDecimal lateFee =
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        // Total
        BigDecimal totalAmount = energyCharge
                .add(fixedCharge)
                .add(fuelSurcharge)
                .add(electricityDuty)
                .add(lateFee)
                .setScale(2, RoundingMode.HALF_UP);

        return Bill.builder()
                .billNumber(generateBillNumber())
                .consumer(consumer)
                .meter(reading.getMeter())
                .meterReading(reading)
                .energyCharge(energyCharge)
                .fixedCharge(fixedCharge)
                .fuelSurcharge(fuelSurcharge)
                .electricityDuty(electricityDuty)
                .lateFee(lateFee)
                .totalAmount(totalAmount)
                .billDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(15))
                .status(BillStatus.GENERATED)
                .build();
    }

    private String generateBillNumber() {
        return "BILL-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}