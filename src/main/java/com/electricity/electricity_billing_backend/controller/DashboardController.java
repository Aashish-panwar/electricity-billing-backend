package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.response.DashboardResponse;
import com.electricity.electricity_billing_backend.dto.response.MonthlyBillsResponse;
import com.electricity.electricity_billing_backend.dto.response.MonthlyUnitsResponse;
import com.electricity.electricity_billing_backend.dto.response.RevenueChartResponse;
import com.electricity.electricity_billing_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<DashboardResponse> getStatistics() {

        return ResponseEntity.ok(
                dashboardService.getDashboardStatistics()
        );

    }

    @GetMapping("/monthly-revenue")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<RevenueChartResponse>> getMonthlyRevenue() {

        return ResponseEntity.ok(

                dashboardService.getMonthlyRevenue()

        );

    }

    @GetMapping("/monthly-units")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<MonthlyUnitsResponse>> getMonthlyUnits() {

        return ResponseEntity.ok(

                dashboardService.getMonthlyUnits()

        );

    }

    @GetMapping("/monthly-bills")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<MonthlyBillsResponse>> getMonthlyBills() {

        return ResponseEntity.ok(

                dashboardService.getMonthlyBills()

        );

    }

}