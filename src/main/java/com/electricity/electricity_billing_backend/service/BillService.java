package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.BillRequest;
import com.electricity.electricity_billing_backend.dto.response.BillResponse;

import java.util.List;

public interface BillService {

    BillResponse generateBill(BillRequest request);

    BillResponse getBillById(Long id);

    BillResponse getBillByNumber(String billNumber);

    List<BillResponse> getAllBills();

    List<BillResponse> getBillsByConsumer(Long consumerId);

    void deleteBill(Long id);

}