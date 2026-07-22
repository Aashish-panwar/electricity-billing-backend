package com.electricity.electricity_billing_backend.mapper;

import com.electricity.electricity_billing_backend.dto.response.BillResponse;
import com.electricity.electricity_billing_backend.entity.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapper {

        public BillResponse toResponse(Bill bill) {

                return BillResponse.builder()

                        .id(bill.getId())

                        .billNumber(bill.getBillNumber())

                        .consumerId(
                                bill.getConsumer().getId()
                        )

                        .consumerName(
                                bill.getConsumer().getFullName()
                        )

                        .meterId(
                                bill.getMeter().getId()
                        )

                        .meterNumber(
                                bill.getMeter().getMeterNumber()
                        )

                        .meterReadingId(
                                bill.getMeterReading().getId()
                        )

                        .unitsConsumed(
                                bill.getMeterReading().getUnitsConsumed()
                        )

                        .energyCharge(
                                bill.getEnergyCharge()
                        )

                        .fixedCharge(
                                bill.getFixedCharge()
                        )

                        .fuelSurcharge(
                                bill.getFuelSurcharge()
                        )

                        .electricityDuty(
                                bill.getElectricityDuty()
                        )

                        .lateFee(
                                bill.getLateFee()
                        )

                        .totalAmount(
                                bill.getTotalAmount()
                        )

                        .billDate(
                                bill.getBillDate()
                        )

                        .dueDate(
                                bill.getDueDate()
                        )

                        .status(
                                bill.getStatus()
                        )

                        .active(
                                bill.getActive()
                        )

                        .build();

        }

}