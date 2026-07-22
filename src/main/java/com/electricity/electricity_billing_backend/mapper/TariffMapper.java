package com.electricity.electricity_billing_backend.mapper;

import com.electricity.electricity_billing_backend.dto.request.TariffRequest;
import com.electricity.electricity_billing_backend.dto.response.TariffResponse;
import com.electricity.electricity_billing_backend.entity.Tariff;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TariffMapper {

    public Tariff toEntity(TariffRequest request) {

        return Tariff.builder()
                .tariffName(request.getTariffName())
                .ratePerUnit(request.getRatePerUnit())
                .fixedCharge(request.getFixedCharge())
                .fuelSurcharge(request.getFuelSurcharge())
                .electricityDuty(request.getElectricityDuty())
                .description(request.getDescription())

                // New Fields
                .effectiveFrom(
                        request.getEffectiveFrom() != null
                                ? request.getEffectiveFrom()
                                : LocalDate.now()
                )
                .effectiveTo(
                        request.getEffectiveTo() != null
                                ? request.getEffectiveTo()
                                : LocalDate.of(2099, 12, 31)
                )

                .build();
    }

    public TariffResponse toResponse(Tariff tariff) {

        return TariffResponse.builder()
                .id(tariff.getId())
                .tariffName(tariff.getTariffName())
                .ratePerUnit(tariff.getRatePerUnit())
                .fixedCharge(tariff.getFixedCharge())
                .fuelSurcharge(tariff.getFuelSurcharge())
                .electricityDuty(tariff.getElectricityDuty())
                .description(tariff.getDescription())

                // New Fields
                .effectiveFrom(tariff.getEffectiveFrom())
                .effectiveTo(tariff.getEffectiveTo())

                .active(tariff.getActive())
                .build();
    }

    public void updateEntity(
            Tariff tariff,
            TariffRequest request
    ) {

        tariff.setTariffName(request.getTariffName());
        tariff.setRatePerUnit(request.getRatePerUnit());
        tariff.setFixedCharge(request.getFixedCharge());
        tariff.setFuelSurcharge(request.getFuelSurcharge());
        tariff.setElectricityDuty(request.getElectricityDuty());
        tariff.setDescription(request.getDescription());

        // New Fields
        if (request.getEffectiveFrom() != null) {
            tariff.setEffectiveFrom(request.getEffectiveFrom());
        }

        if (request.getEffectiveTo() != null) {
            tariff.setEffectiveTo(request.getEffectiveTo());
        }
    }
}