package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.response.ConsumerReportResponse;
import com.electricity.electricity_billing_backend.dto.response.PaymentReportResponse;
import com.electricity.electricity_billing_backend.dto.response.RevenueReportResponse;
import com.electricity.electricity_billing_backend.dto.response.StatisticsReportResponse;
import com.electricity.electricity_billing_backend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    // ==========================================
    // Consumer Report
    // ==========================================

    @Operation(summary = "Generate Consumer Report")
    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<ConsumerReportResponse> getConsumerReport(
            @PathVariable Long consumerId
    ) {
        return ResponseEntity.ok(
                reportService.getConsumerReport(consumerId)
        );
    }

    // ==========================================
    // Revenue Report
    // ==========================================

    @Operation(summary = "Generate Revenue Report")
    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueReportResponse> getRevenueReport(

            @RequestParam Integer month,

            @RequestParam Integer year
    ) {

        return ResponseEntity.ok(
                reportService.getRevenueReport(month, year)
        );
    }

    // ==========================================
    // Payment Report
    // ==========================================

    @Operation(summary = "Generate Payment Report")
    @GetMapping("/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentReportResponse>> getPaymentReport() {

        return ResponseEntity.ok(
                reportService.getPaymentReport()
        );
    }

    // ==========================================
    // Statistics Report
    // ==========================================

    @Operation(summary = "Generate Dashboard Statistics")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatisticsReportResponse> getStatisticsReport() {

        return ResponseEntity.ok(
                reportService.getStatisticsReport()
        );
    }


}