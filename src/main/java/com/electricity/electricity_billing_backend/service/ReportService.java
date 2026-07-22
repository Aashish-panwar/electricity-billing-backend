package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.response.ConsumerReportResponse;
import com.electricity.electricity_billing_backend.dto.response.PaymentReportResponse;
import com.electricity.electricity_billing_backend.dto.response.RevenueReportResponse;
import com.electricity.electricity_billing_backend.dto.response.StatisticsReportResponse;

import java.util.List;

public interface ReportService {

    ConsumerReportResponse getConsumerReport(Long consumerId);

    RevenueReportResponse getRevenueReport(
            Integer month,
            Integer year
    );

    List<PaymentReportResponse> getPaymentReport();

    StatisticsReportResponse getStatisticsReport();

}