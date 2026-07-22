package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.TariffRequest;
import com.electricity.electricity_billing_backend.dto.response.TariffResponse;

import java.util.List;

public interface TariffService {

    TariffResponse createTariff(TariffRequest request);

    TariffResponse updateTariff(Long id, TariffRequest request);

    TariffResponse getTariffById(Long id);

    List<TariffResponse> getAllTariffs();

    void deleteTariff(Long id);

}