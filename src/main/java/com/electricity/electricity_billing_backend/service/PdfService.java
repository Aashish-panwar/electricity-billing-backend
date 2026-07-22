package com.electricity.electricity_billing_backend.service;

public interface PdfService {

    byte[] generateBillPdf(Long billId);

}