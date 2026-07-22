package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.PaymentRequest;
import com.electricity.electricity_billing_backend.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByTransactionId(String transactionId);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByConsumer(Long consumerId);

    List<PaymentResponse> getPaymentsByBill(Long billId);

    void deletePayment(Long id);

}