package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.entity.MeterReading;

public interface BillCalculationService {

    Bill calculateBill(MeterReading meterReading);

}