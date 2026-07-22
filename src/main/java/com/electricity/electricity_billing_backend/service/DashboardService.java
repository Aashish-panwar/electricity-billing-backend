package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.response.DashboardResponse;
import com.electricity.electricity_billing_backend.dto.response.MonthlyBillsResponse;
import com.electricity.electricity_billing_backend.dto.response.MonthlyUnitsResponse;
import com.electricity.electricity_billing_backend.dto.response.RevenueChartResponse;

import java.util.List;

public interface DashboardService {

    DashboardResponse getDashboardStatistics();
    List<RevenueChartResponse> getMonthlyRevenue();
    List<MonthlyUnitsResponse> getMonthlyUnits();
    List<MonthlyBillsResponse> getMonthlyBills();

}